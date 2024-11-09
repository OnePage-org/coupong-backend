package com.onepage.coupong.implementation.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum EventExceptionType {
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    EVENT_NOT_START(HttpStatus.BAD_REQUEST, "400"),
    EVENT_ENDED(HttpStatus.GONE, "410" ),
    EVENT_ALREADY_JOIN(HttpStatus.NOT_ACCEPTABLE, "406" ),
    EVENT_NOT_INITIALIZED(HttpStatus.NOT_ACCEPTABLE, "406" ),
    EVENT_ALREADY_INITIALIZED(HttpStatus.CONFLICT, "409" ),
    EVENT_MANAGER_NOT_INITIALIZED(HttpStatus.NOT_ACCEPTABLE, "406" ),
    EVENT_ATTEMPT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "500" ),
    EVENT_REGISTER_LEADERBOARD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "500" );

    private final HttpStatus status;
    private final String code;
}


