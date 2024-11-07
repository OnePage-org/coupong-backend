package com.onepage.coupong.implementation.leaderboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class EmitterManager {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>(); // Emitter 목록

    // Emitter 추가 및 유효하지 않은 Emitter 제거
    public void addEmitter(SseEmitter emitter) {
        removeInvalidEmitters(); // 유효성 체크
        emitters.add(emitter); // Emitter 추가

        // 완료 및 타임아웃 처리
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });
    }

    // 모든 Emitter에 메시지 전송
    public void sendMessageToEmitters(String message) {
        removeInvalidEmitters(); // 유효성 처리

        // 모든 Emitter에 대해 메시지 전송
        for (SseEmitter emitter : emitters) {
            try {
                if (emitter != null) {
                    log.info("Sending message to emitter: {}", emitter);
                    emitter.send(message); // 메시지 전송
                }
            } catch (IOException e) {
                log.error("Error while sending data: {}", e.getMessage(), e); // 전송 오류
            } catch (IllegalStateException e) {
                log.error("Error with completed emitter: {}", e.getMessage(), e); // 완료된 Emitter 오류
            }
        }
    }

    // 유효하지 않은 Emitter 제거
    private void removeInvalidEmitters() {
        emitters.removeIf(this::isInvalidEmitter);
    }

    // Emitter 유효성 검사
    private boolean isInvalidEmitter(SseEmitter emitter) {
        if (emitter == null) {
            return true; // null인 경우 제거
        }
        try {
            emitter.send(""); // 유효성 검사
            return false; // 유효한 경우 유지
        } catch (IOException e) {
            log.error("Invalid emitter detected and removed: {}", e.getMessage(), e);
            return true; // 유효하지 않은 경우 제거
        }
    }

    // Emitter 제거
    public void removeEmitter(SseEmitter emitter) {
        if (emitter != null) {
            emitters.remove(emitter); // Emitter 제거
        }
    }
}
