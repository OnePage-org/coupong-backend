package com.onepage.coupong.leaderboard.api;

import com.onepage.coupong.coupon.domain.enums.CouponCategory;
import com.onepage.coupong.leaderboard.service.LeaderBoardQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class LeaderboardController {

    private final LeaderBoardQueueService leaderBoardQueueService;

    @Autowired
    public LeaderboardController(LeaderBoardQueueService leaderBoardQueueService) {
        this.leaderBoardQueueService = leaderBoardQueueService;
    }

    // 리더보드 데이터 출력 - 초기 출력
    @GetMapping("/sse/leaderboard")
    public ResponseEntity<Map<String, Map<Object, Double>>> getLeaderboard(@RequestParam String couponCategory) {
        if (couponCategory == null || couponCategory.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 리더보드큐의 값을 읽어와서 저장
        Set<ZSetOperations.TypedTuple<Object>> topWinners = leaderBoardQueueService.getTopRankSetWithScore(couponCategory, 0);

        // 결과를 저장할 맵 생성
        Map<Object, Double> winnersWithScores = new HashMap<>();
        if (topWinners != null) {
            for (ZSetOperations.TypedTuple<Object> tuple : topWinners) {
                winnersWithScores.put(tuple.getValue(), tuple.getScore()); // ID와 스코어를 맵에 추가
            }
        }

        // 결과를 Map으로 반환
        return ResponseEntity.ok(Map.of(couponCategory, winnersWithScores));
    }

    // 리더보드 초기화
    @PostMapping("/sse/leaderboard/clear")
    public ResponseEntity<Void> clearLeaderboard(@RequestParam String couponCategory) {
        // 리더보드큐 초기화
        leaderBoardQueueService.clearLeaderboardQueue(couponCategory);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        // CouponCategory Enum의 모든 값을 리스트로 반환 (DEFAULT 제외)
        List<String> categories = Arrays.stream(CouponCategory.values())
                .filter(category -> category != CouponCategory.DEFAULT) // DEFAULT 제외
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categories);
    }

}
