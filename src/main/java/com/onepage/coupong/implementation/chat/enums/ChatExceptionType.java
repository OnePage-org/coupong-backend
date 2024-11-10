package com.onepage.coupong.implementation.chat.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ChatExceptionType {
    MESSAGE_BLANK(HttpStatus.BAD_REQUEST, "400", "공백 메시지 감지"),
    MESSAGE_TOO_LONG(HttpStatus.BAD_REQUEST, "413", "200자 초과 문자열 감지");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
