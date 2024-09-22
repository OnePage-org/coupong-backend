package com.onepage.coupong.service;

import com.onepage.coupong.dto.LeaderboardUpdateDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class LeaderboardService {

    @Getter
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();  // Flux 스트림을 위한 싱크

    public void updateLeaderboard(LeaderboardUpdateDTO updateDTO) {
        String winnerList = String.join("\", \"", updateDTO.getWinners());
        String message = "data: {\"" + updateDTO.getCouponCategory() + "\": [\"" + winnerList + "\"]}\n\n";
        log.info("Leaderboard update: {}", message);

        // Emit result 체크
        Sinks.EmitResult result = sink.tryEmitNext(message);
        if (result.isFailure()) {
            log.error("Failed to emit message: {}", result); // 에러 로그 추가
        }
    }
}