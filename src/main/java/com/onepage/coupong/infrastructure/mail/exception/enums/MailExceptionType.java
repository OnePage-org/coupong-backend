package com.onepage.coupong.infrastructure.mail.exception.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MailExceptionType {
    MAIL_SEND_ERROR(HttpStatus.BAD_REQUEST, "400");

    private final HttpStatus status;
    private final String code;
}
