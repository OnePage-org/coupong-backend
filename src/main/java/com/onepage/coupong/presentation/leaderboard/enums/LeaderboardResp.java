package com.onepage.coupong.presentation.leaderboard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LeaderboardResp {
    LEADERBOARD_CLEAR_SUCCESS("리더보드 초기화 성공");

    private final String message;
}