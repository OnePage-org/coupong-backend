package com.onepage.coupong.chat.controller;


import com.onepage.coupong.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;

@Controller @RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessagingTemplate template;
//    private final Map<Long, String> users = new ConcurrentHashMap<>(); // 참여자 Map
    private final HashSet<String> users = new HashSet<>(); // 참여자 Set
    public void updateUserCnt() { // 참여자 수 갱신
        int userCnt = users.size();
        template.convertAndSend("/sub/users", userCnt);
    }
    @MessageMapping(value = "/enter") // 입장 메시지
    public void userEnter(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        message.setMessage(message.getWriter()+"님이 입장하였습니다.");
        message.setCreatedDate(formattedDate);

        users.add(message.getWriter());

        ChatMessage chatMessage = new ChatMessage("알림", message.getMessage(), message.getCreatedDate());
        template.convertAndSend("/sub/chat", chatMessage);
        
        updateUserCnt(); // 참여자 갱신
    }

    @MessageMapping(value = "/messages") // 메시지 전송
    public void sendMessage(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        message.setCreatedDate(formattedDate);
        template.convertAndSend("/sub/chat", message);
    }

    @MessageMapping(value = "/exit") // 퇴장 메시지
    public void userExit(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        users.remove(message.getWriter());

        message.setWriter(message.getWriter());
        message.setMessage(message.getWriter()+"님이 퇴장하였습니다.");
        message.setCreatedDate(formattedDate);

        ChatMessage chatMessage = new ChatMessage("알림", message.getMessage(), message.getCreatedDate());
        template.convertAndSend("/sub/chat", chatMessage);
        updateUserCnt(); // 참여자 갱신

    }
}