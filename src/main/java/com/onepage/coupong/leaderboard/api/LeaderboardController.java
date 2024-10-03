package com.onepage.coupong.leaderboard.api;

import com.onepage.coupong.leaderboard.dto.AddToZSetRequest;
import com.onepage.coupong.leaderboard.dto.LeaderboardUpdateDto;
import com.onepage.coupong.leaderboard.service.LeaderBoardQueueService;
import com.onepage.coupong.leaderboard.service.LeaderboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class LeaderboardController {

    private final LeaderBoardQueueService leaderBoardQueueService;
    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderBoardQueueService leaderBoardQueueService, LeaderboardService leaderboardService) {
        this.leaderBoardQueueService = leaderBoardQueueService;
        this.leaderboardService = leaderboardService;
    }

    // 리더보드 데이터 가져오기
    @GetMapping("/sse/leaderboard")
    public ResponseEntity<Map<String, Set<Object>>> getLeaderboard(@RequestParam String couponCategory) {
        if (couponCategory == null || couponCategory.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // 리더보드 데이터 가져오기
        Set<Object> topWinners = leaderBoardQueueService.getZSet(couponCategory);

        return ResponseEntity.ok(Map.of(couponCategory, topWinners)); // 간결한 반환
    }

    // 리더보드 클리어
    @PostMapping("/sse/leaderboard/clear")
    public ResponseEntity<Void> clearLeaderboard(@RequestParam String couponCategory) {
        leaderBoardQueueService.clearLeaderboardQueue(couponCategory);

        // 클리어 후 리더보드 업데이트
        Set<Object> topWinners = leaderBoardQueueService.getZSet(couponCategory);
        List<String> winnerList = topWinners.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        LeaderboardUpdateDto updateDTO = new LeaderboardUpdateDto(couponCategory, winnerList);
        leaderboardService.updateLeaderboard(updateDTO);
        return ResponseEntity.ok().build();
    }

    // 리더보드에 추가
    @PostMapping("/leaderboardqueueservice/addtozset")
    public ResponseEntity<Boolean> addToZSet(@RequestBody AddToZSetRequest request) {
        String couponCategory = request.getCouponCategory();
        String userId = request.getUserId();
        double attemptAt = request.getAttemptAt();

        boolean result = leaderBoardQueueService.addToZSet(couponCategory, userId, attemptAt);
        return ResponseEntity.ok(result);
    }
}
