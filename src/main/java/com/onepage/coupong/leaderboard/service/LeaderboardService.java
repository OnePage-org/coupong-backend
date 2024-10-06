package com.onepage.coupong.leaderboard.service;

import com.onepage.coupong.leaderboard.dto.LeaderboardUpdateResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class LeaderboardService {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer(); // Flux 스트림을 위한 싱크
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>(); // Emitter 목록

    public void updateLeaderboard(LeaderboardUpdateResponseDto updateDTO) {
        List<String> winners = updateDTO.getWinners();

        // 메시지 형식 수정: 빈 배열로 설정
        String message = String.format("{\"couponCategory\": \"%s\", \"winners\": %s}",
                updateDTO.getCouponCategory(),
                winners.isEmpty() ? "[]" : String.format("[\"%s\"]", String.join("\",\"", winners)));

        // Emit result 체크
        Sinks.EmitResult result = sink.tryEmitNext(message);
        if (result.isFailure()) {
            log.error("Failed to emit message: {}", message);
        } else {
            log.info("Successfully emitted message: {}", message);
            // 메시지를 Emitter에 전송
            sendMessageToEmitters(message); // 메시지를 직접 전송
        }
    }

    // 메시지를 Emitter에 전송하는 메서드
    public void sendMessageToEmitters(String message) {
        // 이터레이터 대신 removeIf를 사용하여 안전하게 제거
        emitters.removeIf(emitter -> {
            try {
                // 메시지를 보내기 전에 emitter가 유효한지 확인
                if (emitter != null) {
                    emitter.send(message);
                    return false; // 유효한 emitter는 유지
                }
            } catch (IOException e) {
                log.error("Broken pipe error while sending message to emitter: {}", emitter, e);
                return true; // 오류 발생 시 제거
            } catch (IllegalStateException e) {
                log.warn("Emitter has already completed: {}", emitter);
                return true; // 완료된 emitter 제거
            }
            return false; // null인 경우 유지
        });
    }


    public void addEmitter(SseEmitter emitter) {
        // 기존 Emitter가 있는지 확인하고, 있으면 제거
        emitters.removeIf(existingEmitter -> {
            if (existingEmitter == null) {
                return true; // null인 경우 제거
            }
            try {
                existingEmitter.send(""); // 유효한 Emitter인지 확인
                return false; // 유효한 경우 유지
            } catch (IOException e) {
                return true; // 완료된 경우 제거
            }
        });

        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });
    }

}
