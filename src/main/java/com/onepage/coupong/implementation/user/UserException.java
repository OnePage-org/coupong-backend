package com.onepage.coupong.implementation.user;

import com.onepage.coupong.global.exception.CustomRuntimeException;
import com.onepage.coupong.implementation.user.enums.UserExceptionType;

public class UserException extends CustomRuntimeException {
    public UserException(UserExceptionType message, Object... args) {super(String.valueOf(message), args);}
}
