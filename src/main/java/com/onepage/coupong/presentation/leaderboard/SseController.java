package com.onepage.coupong.presentation.leaderboard;

import com.onepage.coupong.implementation.leaderboard.EmitterManager;
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

    private final EmitterManager emitterManager; // EmitterManager 주입
    private static final String INITIAL_MESSAGE = "{\"message\": \"연결되었습니다\"}"; // 초기 메시지

    public SseController(EmitterManager emitterManager) {
        this.emitterManager = emitterManager;
    }

    // 클라이언트에서 리더보드 업데이트를 위한 SSE 연결 설정
    @GetMapping(value = "/sse/leaderboard/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLeaderboardUpdates(@RequestParam String couponCategory) {
        log.info("Client connected to leaderboard stream for category: {}", couponCategory);

        // 클라이언트에게 emitter를 생성하고 등록
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitterManager.addEmitter(emitter);

        try {
            // 초기 메시지 전송
            emitter.send(INITIAL_MESSAGE);
        } catch (IOException e) {
            log.error("Error sending initial message to emitter: ", e);
            emitterManager.removeEmitter(emitter); // Emitter 제거
        }

        return emitter; // 클라이언트에게 emitter 반환
    }
}
