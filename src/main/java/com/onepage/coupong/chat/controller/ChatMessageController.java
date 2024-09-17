package com.onepage.coupong.chat.controller;


import com.onepage.coupong.chat.entity.ChatMessage;
import com.onepage.coupong.sign.entity.CustomOAuth2User;
import com.onepage.coupong.sign.provider.JwtProvider;
import com.onepage.coupong.sign.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller @RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessagingTemplate template;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final Map<String, String> userSessions = new ConcurrentHashMap<>(); // 참여자 Map

    public void updateUserCnt() { // 참여자 수 갱신
        int userCnt = userSessions.size();
        template.convertAndSend("/sub/users", userCnt);
    }

    @MessageMapping(value = "/enter") // 입장 메시지
    public void userEnter(ChatMessage message, SimpMessageHeaderAccessor accessor) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        CustomOAuth2User principal = (CustomOAuth2User) accessor.getUser();
        System.out.println(principal);

        message.setMessage(message.getWriter()+"님이 입장하였습니다.");
        message.setCreatedDate(formattedDate);

        ChatMessage chatMessage = new ChatMessage("알림", message.getMessage(), message.getCreatedDate());
        template.convertAndSend("/sub/chat", chatMessage);

        // 참여자 갱신
        updateUserCnt();
    }

    @MessageMapping(value = "/messages") // 메시지 전송
    public void sendMessage(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        message.setCreatedDate(formattedDate);
        template.convertAndSend("/sub/chat", message);
    }

    @MessageMapping(value = "/exit")
    public void userExit(ChatMessage message, SimpMessageHeaderAccessor accessor) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        String token = accessor.getFirstNativeHeader("Authorization");
        String username = jwtProvider.validate(token);

        if (username == null) return;

        userSessions.remove(username);

        message.setWriter(username);
        message.setMessage(username+"님이 퇴장하였습니다.");
        message.setCreatedDate(formattedDate);

        ChatMessage chatMessage = new ChatMessage("알림", message.getMessage(), message.getCreatedDate());
        template.convertAndSend("/sub/chat", chatMessage);
        updateUserCnt(); // 참여자 갱신

    }
}