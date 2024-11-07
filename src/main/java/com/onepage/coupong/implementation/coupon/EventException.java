package com.onepage.coupong.implementation.coupon;

import com.onepage.coupong.implementation.coupon.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventException extends RuntimeException {
    ErrorCode errorCode;

    public EventException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
