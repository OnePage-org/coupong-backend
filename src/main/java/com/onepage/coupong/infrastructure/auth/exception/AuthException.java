package com.onepage.coupong.infrastructure.auth.exception;

import com.onepage.coupong.global.exception.CustomRuntimeException;
import com.onepage.coupong.infrastructure.auth.exception.enums.AuthExceptionType;

public class AuthException extends CustomRuntimeException {
    public AuthException(AuthExceptionType message, Object... args) {super(String.valueOf(message), args);}
}
