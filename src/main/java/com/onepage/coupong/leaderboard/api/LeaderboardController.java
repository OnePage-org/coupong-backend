package com.onepage.coupong.leaderboard.api;

import com.onepage.coupong.leaderboard.service.LeaderBoardQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

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
    public ResponseEntity<Map<String, Set<Object>>> getLeaderboard(@RequestParam String couponCategory) {
        if (couponCategory == null || couponCategory.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 리더보드큐의 값을 읽어와서 저장
        Set<Object> topWinners = leaderBoardQueueService.getZSet(couponCategory);
        return ResponseEntity.ok(Map.of(couponCategory, topWinners));
    }

    // 리더보드 초기화
    @PostMapping("/sse/leaderboard/clear")
    public ResponseEntity<Void> clearLeaderboard(@RequestParam String couponCategory) {
        // 리더보드큐 초기화
        leaderBoardQueueService.clearLeaderboardQueue(couponCategory);
        return ResponseEntity.ok().build();
    }
}
