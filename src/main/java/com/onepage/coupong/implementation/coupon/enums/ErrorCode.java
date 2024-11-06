package com.onepage.coupong.implementation.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EVENT_NOT_START(HttpStatus.BAD_REQUEST, "400"),
    EVENT_ENDED(HttpStatus.GONE, "410" ),
    EVENT_ALREADY_JOIN(HttpStatus.NOT_ACCEPTABLE, "406" );

    private final HttpStatus status;
    private final String code;
}
