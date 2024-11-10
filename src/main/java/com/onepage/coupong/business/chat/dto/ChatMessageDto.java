package com.onepage.coupong.business.chat.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageDto {
    private String writer;
    private String message;
    private String createdDate;
}