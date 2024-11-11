package com.onepage.coupong.implementation.user;

import com.onepage.coupong.global.exception.CustomRuntimeException;
import com.onepage.coupong.implementation.user.enums.CertificationExceptionType;

public class CertificationException extends CustomRuntimeException {
    public CertificationException(CertificationExceptionType message, Object... args) {super(String.valueOf(message), args);}
}
