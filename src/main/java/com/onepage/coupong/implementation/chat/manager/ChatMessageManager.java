package com.onepage.coupong.implementation.chat.manager;

import com.onepage.coupong.business.chat.dto.ChatMessageDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class ChatMessageManager {
    public ChatMessageDto createEnterMessage(String username) {
        return ChatMessageDto.builder()
                .type("입장")
                .message(username + "님이 입장하였습니다.")
                .writer(username)
                .createdDate("")
                .build();
    }

    public ChatMessageDto createExitMessage(String username) {
        return ChatMessageDto.builder()
                .type("퇴장")
                .message(username + "님이 퇴장하였습니다.")
                .writer(username)
                .createdDate("")
                .build();
    }

    public ChatMessageDto createChatMessage(String message, String username, String type) {
        return ChatMessageDto.builder()
                .type(type == null ? "메시지" : type)
                .message(message)
                .writer(username)
                .createdDate(getCurrentTime())
                .build();
    }

    public String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH);
        return LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);
    }
}
