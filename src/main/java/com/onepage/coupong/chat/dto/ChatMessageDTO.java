package com.onepage.coupong.chat.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatMessageDTO {
    private String writer;
    private String message;
    private String createdDate;
}