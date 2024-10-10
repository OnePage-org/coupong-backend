package com.onepage.coupong.leaderboard.service;

import com.onepage.coupong.leaderboard.EmitterManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LeaderboardService {

    private final EmitterManager emitterManager; // EmitterManager 주입

    @Autowired
    public LeaderboardService(EmitterManager emitterManager) {
        this.emitterManager = emitterManager;
    }

    // 리더보드 업데이트
    public void updateLeaderboard(String couponCategory, Set<Object> topWinners, double entryTime) {
        String message = createLeaderboardUpdateMessage(couponCategory, topWinners, entryTime);
        emitterManager.sendMessageToEmitters(message); // EmitterManager를 통해 메시지 전송
    }

    // 리더보드 업데이트 메시지 생성
    private String createLeaderboardUpdateMessage(String couponCategory, Set<Object> topWinners, double entryTime) {
        List<String> winners = topWinners.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        // 당첨자 리스트가 비어있으면 빈 배열, 아니면 JSON 형식으로 메시지 생성
        String winnersJson = winners.isEmpty() ? "[]" : String.format("[\"%s\"]", String.join("\",\"", winners));
        String message = String.format("{\"couponCategory\": \"%s\", \"winners\": %s, \"entryTime\": %f}",
                couponCategory, winnersJson, entryTime);

        return message; // 생성된 메시지 반환
    }
}
