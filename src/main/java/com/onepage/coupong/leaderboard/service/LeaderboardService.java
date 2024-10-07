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
    public void updateLeaderboard(String couponCategory, Set<Object> topWinners) {
        String message = createLeaderboardUpdateMessage(couponCategory, topWinners);
        emitterManager.sendMessageToEmitters(message); // EmitterManager 사용
    }

    // 리더보드 업데이트 메시지 생성
    private String createLeaderboardUpdateMessage(String couponCategory, Set<Object> topWinners) {
        List<String> winners = topWinners.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        // 메시지 생성
        // 당첨자 리스트가 비었으면 빈배열을, 아니면 당첨자를 json 파싱 형식에 맞춰서 메시지 생성
        String winnersJson = winners.isEmpty() ? "[]" : String.format("[\"%s\"]", String.join("\",\"", winners));
        String message = String.format("{\"couponCategory\": \"%s\", \"winners\": %s}", couponCategory, winnersJson);

        return message; // 생성된 메시지 반환
    }
}
