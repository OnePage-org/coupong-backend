package com.onepage.coupong.leaderboard.api;

import com.onepage.coupong.leaderboard.service.LeaderboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@RestController
public class SseController {

    private final LeaderboardService leaderboardService;

    public SseController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping(value = "/sse/leaderboard/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLeaderboardUpdates(@RequestParam String couponCategory) {
        log.info("Client connected to leaderboard stream for category: {}", couponCategory);

        // 클라이언트에게 emitter를 등록하여 데이터 전송
        SseEmitter emitter = leaderboardService.registerEmitter();

        // 에미터가 완료되거나 타임아웃될 때 로그를 기록합니다.
        emitter.onCompletion(() -> log.info("Emitter completed for category: {}", couponCategory));
        emitter.onTimeout(() -> {
            log.warn("Emitter timed out for category: {}", couponCategory);
            emitter.complete(); // 타임아웃 발생 시 에미터 종료
        });

        return emitter; // 클라이언트에게 에미터 반환
    }
}