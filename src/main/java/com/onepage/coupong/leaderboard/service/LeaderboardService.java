package com.onepage.coupong.leaderboard.service;

import com.onepage.coupong.leaderboard.dto.LeaderboardUpdateResponseDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Service
public class LeaderboardService {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();  // Flux 스트림을 위한 싱크
    private final List<SseEmitter> emitters = new ArrayList<>();

    public LeaderboardService() {

        sink.asFlux().subscribe(update -> {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(update); // 구독자에게 메시지 전송
                } catch (IOException e) {
                    log.error("Error sending message to emitter: ", e);
                    emitters.remove(emitter); // 에러 발생 시 emitter 제거
                }
            }
        });
    }

    public void updateLeaderboard(LeaderboardUpdateResponseDto updateDTO) {
        String winnerList = updateDTO.getWinners().isEmpty()
                ? ""
                : String.join("\", \"", updateDTO.getWinners());
        String message = String.format("{\"couponCategory\": \"%s\", \"winners\": [\"%s\"]}",
                updateDTO.getCouponCategory(), winnerList);

        // Emit result 체크
        Sinks.EmitResult result = sink.tryEmitNext(message);
        if (result.isFailure()) {
            log.error("Failed to emit message: {}", message);
        }
    }

    public SseEmitter registerEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        // emitter가 종료될 때 emitter 목록에서 제거
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }
}