package com.onepage.coupong.chat.controller;


import com.onepage.coupong.chat.entity.ChatMessage;
import com.onepage.coupong.chat.response.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller @RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessagingTemplate template;
    private final Map<String, Boolean> users = new ConcurrentHashMap<>(); // 참여자 Map -> 동시성을 위한 ConcurrentHashMap사용
    private final int MAX_MESSAGE_LENGTH = 200;

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

        users.put(message.getWriter(), Boolean.TRUE);

        ChatMessage chatMessage = new ChatMessage("알림", message.getMessage(), message.getCreatedDate());
        template.convertAndSend("/sub/chat", chatMessage);

        updateUserCnt(); // 참여자 갱신
    }

    @MessageMapping(value = "/messages") // 메시지 전송
    public void sendMessage(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        message.setCreatedDate(formattedDate);

        ChatResponseDto dto = new ChatResponseDto(message);
        System.out.println(ResponseEntity.ok(dto));

        template.convertAndSend("/sub/chat", message);
    }

    @PostMapping("/api/v1/filtering")
    public ResponseEntity<?> filterMessage(@RequestBody ChatResponseDto chatResponseDto) {

        ChatMessage message = chatResponseDto.getData();
        
        if (message.getMessage().trim().length() == 0){ // 공백만 가면 
            return ChatResponseDto.noChat(chatResponseDto.getData());
        } else if (message.getMessage().length() > 200) { // 200글자가 넘어가면
            return ChatResponseDto.tooLongChat(chatResponseDto.getData());
        }
        
        return ChatResponseDto.successChat(chatResponseDto.getData()); // 정상
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