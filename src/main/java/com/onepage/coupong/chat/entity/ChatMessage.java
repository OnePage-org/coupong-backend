package com.onepage.coupong.chat.entity;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatMessage {
    private String writer;
    private String message;
    private String createdDate;
}