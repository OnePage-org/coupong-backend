package com.onepage.coupong.implementation.user.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserExceptionType {
    UNAVAILABLE_USERNAME(HttpStatus.BAD_REQUEST, "400"),
    UNAVAILABLE_PASSWORD(HttpStatus.BAD_REQUEST, "400"),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "400"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404");

    private final HttpStatus status;
    private final String code;

}
