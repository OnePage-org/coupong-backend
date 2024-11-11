package com.onepage.coupong.implementation.leaderboard.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class EmitterManager {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter addEmitter() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(sseEmitter);

        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            emitters.remove(sseEmitter);
        });

        return sseEmitter;
    }

    public void sendMessageToEmitters(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                if (emitter != null) {
                    emitter.send(message);
                }
            } catch (Exception e) {
                validateAndRemoveEmitters();
            }
        }
    }

    public void validateAndRemoveEmitters() {
        emitters.removeIf(this::isInvalidEmitter);
    }

    private boolean isInvalidEmitter(SseEmitter emitter) {
        if (emitter == null) {
            return true;
        }
        try {
            emitter.send("");
            return false;
        } catch (Exception e) {
            return true;
        }
    }

}
