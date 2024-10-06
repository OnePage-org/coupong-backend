package com.onepage.coupong.leaderboard.api;

import com.onepage.coupong.leaderboard.service.LeaderboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

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
        SseEmitter emitter = new SseEmitter();

        // Emitter를 리더보드 서비스에 추가
        leaderboardService.addEmitter(emitter);

        try {
            // JSON 형식으로 초기 메시지를 전송
            emitter.send("{\"message\": \"연결되었습니다\"}"); // JSON 형식으로 전송
        } catch (IOException e) {
            log.error("Error sending initial message to emitter: ", e);
            // 에러 발생 시 Emitter는 서비스에서 제거됨
        }


        return emitter; // 클라이언트에게 emitter 반환
    }
}