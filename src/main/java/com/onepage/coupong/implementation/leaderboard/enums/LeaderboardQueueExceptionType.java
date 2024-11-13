package com.onepage.coupong.implementation.leaderboard.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum LeaderboardQueueExceptionType {

    REDIS_ZSET_ADD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "500");

    private final HttpStatus status;
    private final String code;
}
