package com.onepage.coupong.presentation.leaderboard;

import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import com.onepage.coupong.business.leaderboard.LeaderboardQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardQueueService leaderBoardQueueService;

    // 리더보드 데이터 출력 (초기 출력)
    @GetMapping("/sse/leaderboard")
    public ResponseEntity<Map<String, Map<Object, Double>>> getLeaderboard(@RequestParam String couponCategory) {
        if (couponCategory == null || couponCategory.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 잘못된 요청 처리
        }

        // 리더보드의 상위 승자 가져오기
        Set<ZSetOperations.TypedTuple<Object>> topWinners = leaderBoardQueueService.getTopRankSetWithScore(couponCategory, 0);
        Map<Object, Double> winnersWithScores = new HashMap<>();

        // 승자와 스코어를 맵에 추가
        if (topWinners != null) {
            for (ZSetOperations.TypedTuple<Object> tuple : topWinners) {
                winnersWithScores.put(tuple.getValue(), tuple.getScore());
            }
        }

        return ResponseEntity.ok(Map.of(couponCategory, winnersWithScores)); // 결과 반환
    }

    // 리더보드 초기화
    @PostMapping("/sse/leaderboard/clear")
    public ResponseEntity<Void> clearLeaderboard(@RequestParam String couponCategory) {
        leaderBoardQueueService.clearLeaderboardQueue(couponCategory); // 리더보드 큐 초기화
        return ResponseEntity.ok().build();
    }

    // 모든 카테고리 반환 (DEFAULT 제외)
    @GetMapping("/api/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = Arrays.stream(CouponCategory.values())
                .filter(category -> category != CouponCategory.DEFAULT)
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categories);
    }
}
