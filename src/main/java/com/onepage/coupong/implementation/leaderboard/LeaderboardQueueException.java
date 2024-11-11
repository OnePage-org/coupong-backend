package com.onepage.coupong.implementation.leaderboard;

import com.onepage.coupong.global.exception.CustomRuntimeException;
import com.onepage.coupong.implementation.leaderboard.enums.LeaderboardQueueExceptionType;

public class LeaderboardQueueException extends CustomRuntimeException {
    public LeaderboardQueueException(LeaderboardQueueExceptionType message, Object... args) {
        super(String.valueOf(message), args);
    }
}
