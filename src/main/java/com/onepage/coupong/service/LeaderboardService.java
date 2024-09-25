package com.onepage.coupong.service;

import com.onepage.coupong.dto.LeaderboardUpdateDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Slf4j
@Getter
@Service
public class LeaderboardService {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();  // Flux 스트림을 위한 싱크

    public LeaderboardService() {
        // 구독자 수 확인을 위한 로그
        sink.asFlux().subscribe(update -> {
            log.info("New subscriber connected.");
        });
    }

    public void updateLeaderboard(LeaderboardUpdateDTO updateDTO) {
        String winnerList = updateDTO.getWinners().isEmpty()
                ? ""
                : String.join("\", \"", updateDTO.getWinners());
        String message = String.format("{\"couponCategory\": \"%s\", \"winners\": [\"%s\"]}",
                updateDTO.getCouponCategory(), winnerList);
        log.info("Leaderboard update: {}", message);

        // Emit result 체크
        Sinks.EmitResult result = sink.tryEmitNext(message);
        if (result.isFailure()) {
            log.error("Failed to emit message: {}", result); // 에러 로그 추가
        } else {
            log.info("Message emitted successfully");
        }
    }
}