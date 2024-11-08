package com.onepage.coupong.business.chat.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatMessageDto {
    private String writer;
    private String message;
    private String createdDate;
}