package com.onepage.coupong.controller;

import com.onepage.coupong.dto.LeaderboardUpdateDTO;
import com.onepage.coupong.service.LeaderBoardQueueService;
import com.onepage.coupong.service.LeaderboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
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

        Map<String, Set<Object>> topWinners = new HashMap<>();
        topWinners.put(couponCategory, leaderBoardQueueService.getZSet(couponCategory));

        if (topWinners.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topWinners);
    }

    // 리더보드 클리어
    @PostMapping("/sse/leaderboard/clear")
    public ResponseEntity<Void> clearLeaderboard(@RequestParam String couponCategory) {
        leaderBoardQueueService.clearLeaderboardQueue(couponCategory);
        Set<Object> topWinners = leaderBoardQueueService.getZSet(couponCategory);
        List<String> winnerList = topWinners.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        LeaderboardUpdateDTO updateDTO = new LeaderboardUpdateDTO(couponCategory, winnerList);
        leaderboardService.updateLeaderboard(updateDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/sse/leaderboard/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLeaderboardUpdates(@RequestParam String couponCategory) {
        // 5초마다 리더보드 업데이트 전송
        Flux.interval(Duration.ofSeconds(5))
                .doOnNext(tick -> {
                    // 리더보드 조회
                    Set<Object> topWinners = leaderBoardQueueService.getZSet(couponCategory);

                    List<String> winnerList = topWinners.stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());

                    // 리더보드 업데이트를 sink에 발행

                    leaderboardService.updateLeaderboard(new LeaderboardUpdateDTO(couponCategory, winnerList));

                })
                .subscribe();  // 주기적으로 리더보드 업데이트를 발행

        // 클라이언트에게 sink를 통해 데이터 전송
        return leaderboardService.getSink().asFlux()
                .replay(1)  // 마지막 1개의 이벤트를 캐시하여 새로운 구독자에게 전달
                .autoConnect();
    }
}