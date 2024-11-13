package com.onepage.coupong.infrastructure.mail.exception;

import com.onepage.coupong.global.exception.CustomRuntimeException;
import com.onepage.coupong.infrastructure.mail.exception.enums.MailExceptionType;

public class MailException extends CustomRuntimeException {
    public MailException(MailExceptionType message, Object... args) {super(String.valueOf(message), args);}
}
