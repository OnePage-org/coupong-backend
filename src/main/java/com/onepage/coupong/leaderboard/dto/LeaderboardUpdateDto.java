package com.onepage.coupong.leaderboard.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class LeaderboardUpdateDto {
    // Getter
    @Getter
    private final String couponCategory; // 변경: final로 선언
    private final List<String> winners;   // 변경: final로 선언

    // 생성자
    public LeaderboardUpdateDto(String couponCategory, List<String> winners) {
        this.couponCategory = couponCategory;
        this.winners = winners != null ? List.copyOf(winners) : Collections.emptyList(); // 변경: 불변 리스트로 설정
    }

    public List<String> getWinners() {
        return Collections.unmodifiableList(winners); // 변경: 불변 리스트로 반환
    }
}