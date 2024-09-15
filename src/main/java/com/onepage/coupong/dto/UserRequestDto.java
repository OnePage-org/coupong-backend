package com.onepage.coupong.dto;

import jdk.jfr.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String id;
    private String username;
    private String email;
    private EventType eventType;
    private long attemptAt = System.currentTimeMillis();
}
