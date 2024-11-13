package com.onepage.coupong.implementation.user.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CertificationExceptionType {

    CERTIFICATION_UNAVAILABLE(HttpStatus.BAD_REQUEST, "400"),
    CERTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "404");

    private final HttpStatus status;
    private final String code;
}
