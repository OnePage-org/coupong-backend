package com.onepage.coupong.business.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class ChatMessageDto {
    private String type;
    private String writer;
    private String message;
    private String createdDate;
}