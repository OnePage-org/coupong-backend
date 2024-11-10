package com.onepage.coupong.implementation.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ChatExceptionType {
    MESSAGE_BLANK(HttpStatus.BAD_REQUEST, "공백 메시지 감지"),
    MESSAGE_TOO_LONG(HttpStatus.BAD_REQUEST, "200자 초과 문자열 감지");

    private final HttpStatus status;
    private final String message;

}
