package com.onepage.coupong.presentation.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventControllerResp {
    EVENT_ATTEMPT_SUCCESS("쿠폰 발행 요청 성공");

    private String message;
}

