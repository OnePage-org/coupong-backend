package com.onepage.coupong.infrastructure.mail.exception.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MailExceptionType {
    MAIL_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500");

    private final HttpStatus status;
    private final String code;
}
