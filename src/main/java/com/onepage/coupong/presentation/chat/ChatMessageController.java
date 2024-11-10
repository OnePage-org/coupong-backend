package com.onepage.coupong.presentation.chat;

import com.onepage.coupong.business.chat.dto.FilteringRequestDto;
import com.onepage.coupong.business.chat.dto.ChatMessageDto;
import com.onepage.coupong.implementation.chat.ChatException;
import com.onepage.coupong.implementation.chat.enums.ChatExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller @RequiredArgsConstructor
public class ChatMessageController {

    private final ChatUseCase chatUseCase;

    @MessageMapping(value = "/enter")
    public void userEnter(ChatMessageDto message, StompHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getWriter());
        chatUseCase.sendMessage(chatUseCase.userEnter(message.getWriter()));
    }

    @MessageMapping(value = "/messages")
    public void sendMessage(ChatMessageDto message) {
        chatUseCase.sendMessage(message);
    }

    @PostMapping("/api/v1/filtering")
    public ResponseEntity<?> filterMessage(@RequestBody FilteringRequestDto filteringRequestDTO) throws Exception {

        String message = filteringRequestDTO.getMessage();

        if (message.trim().isEmpty()){
            throw new ChatException(ChatExceptionType.MESSAGE_BLANK);
        } else if (message.length() > 200) {
            throw new ChatException(ChatExceptionType.MESSAGE_TOO_LONG);
        }

        if(chatUseCase.filteringChatMessage(message)) {
            return ResponseEntity.ok("금칙어에 해당합니다.");
        } else {
            return ResponseEntity.ok("금칙어에 해당하지 않습니다.");
        }
    }

    @EventListener
    public void userExit(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) accessor.getSessionAttributes().get("username");

        if (username != null) {
            chatUseCase.sendMessage(chatUseCase.userExit(username));
        }
    }

}