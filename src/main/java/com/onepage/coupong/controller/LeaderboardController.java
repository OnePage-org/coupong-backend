package com.onepage.coupong.controller;

import com.onepage.coupong.dto.LeaderboardUpdateDTO;
import com.onepage.coupong.service.LeaderBoardQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class LeaderboardController {

    private final LeaderBoardQueueService leaderBoardQueueService;

    @Autowired
    public LeaderboardController(LeaderBoardQueueService leaderBoardQueueService) {
        this.leaderBoardQueueService = leaderBoardQueueService;
    }

    // 리더보드 데이터 가져오기
    @GetMapping("/api/leaderboard")
    public ResponseEntity<Map<String, Set<Object>>> getLeaderboard(@RequestParam String couponCategory) {
        if (couponCategory == null || couponCategory.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Map<String, Set<Object>> topWinners = new HashMap<>();
        topWinners.put(couponCategory, leaderBoardQueueService.getZSet(couponCategory));

        if (topWinners.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topWinners);
    }

    // 리더보드 클리어
    @PostMapping("/api/leaderboard/clear")
    public ResponseEntity<Void> clearLeaderboard(@RequestParam String couponCategory) {
        leaderBoardQueueService.clearLeaderboard(couponCategory);
        Set<Object> topWinners = leaderBoardQueueService.getZSet(couponCategory);
        List<String> winnerList = topWinners.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        LeaderboardUpdateDTO updateDTO = new LeaderboardUpdateDTO(couponCategory, winnerList);
        leaderBoardQueueService.updateLeaderboard(updateDTO);
        return ResponseEntity.ok().build();
    }

    // SSE를 통해 리더보드 업데이트 전송
    @GetMapping(value = "/sse/leaderboard", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLeaderboardUpdates(@RequestParam String couponCategory) {
        log.info("SSE request received for category: {}", couponCategory);
        return leaderBoardQueueService.getSink().asFlux().filter(data -> data.contains(couponCategory));
    }
}