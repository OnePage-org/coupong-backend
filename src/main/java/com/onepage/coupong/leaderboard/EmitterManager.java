package com.onepage.coupong.leaderboard;

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


    // Emitter를 추가, 유효하지 않은 Emitter는 제거
    public void addEmitter(SseEmitter emitter) {
        // 유효성 체크
        removeInvalidEmitters();
        // 이미터 추가
        emitters.add(emitter);

        // 완료와 타임아웃 처리
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });
    }


    // 모든 Emitter에 메시지를 전송
    public void sendMessageToEmitters(String message) {
        // 유효성 처리
        removeInvalidEmitters();

        // 관리되고있는 모든 이미터에 대해
        for (SseEmitter emitter : emitters) {
            try {
                // 이미터가 null이 아니면 메시지 전송
                if (emitter != null) {
                    emitter.send(message);
                }
            } catch (IOException e) {
                // broken pipe 에러
                log.error("Error while sending data: {}", e.getMessage(), e);
                // 필요에 따라 예외를 다시 던질 수 있습니다.
            } catch (IllegalStateException e) {
                // completed emitter 에러
                log.error("Error with completed emitter: {}", e.getMessage(), e);
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
            emitter.send(""); // 유효성 검사 ( 메시지가 보내지나 확인 )
            return false; // 유효한 경우 유지
        } catch (IOException e) {
            log.error("Invalid emitter detected and removed: {}", e.getMessage(), e);
            return true; // 유효하지 않은 경우 제거
        }
    }

    // Emitter 제거 (sse 연결 후 확인 메시지를 보낼 때 오류가 발생하면 이미터를 바로 삭제하는 부분)
    public void removeEmitter(SseEmitter emitter) {
        if (emitter != null) {
            emitters.remove(emitter);
        }
    }

}
