package com.onepage.coupong.implementation.coupon;

import com.onepage.coupong.global.exception.CustomRuntimeException;
import com.onepage.coupong.implementation.coupon.enums.EventExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class EventException extends CustomRuntimeException {
    public EventException(EventExceptionType message, Object... args) {super(String.valueOf(message), args);}
}
