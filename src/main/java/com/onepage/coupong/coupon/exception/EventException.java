package com.onepage.coupong.coupon.exception;

import com.onepage.coupong.coupon.exception.enums.ErrorCode;
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
