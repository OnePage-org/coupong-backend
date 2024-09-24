package com.onepage.coupong.chat.controller;


import com.onepage.coupong.chat.ChatService;
import com.onepage.coupong.chat.dto.FilteringRequestDTO;
import com.onepage.coupong.chat.entity.ChatMessage;
import com.onepage.coupong.chat.response.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private ChatService chatService;

    private final SimpMessagingTemplate template;
    private final Map<String, Boolean> users = new ConcurrentHashMap<>(); // 참여자 Map -> 동시성을 위한 ConcurrentHashMap사용

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

        ChatMessage chatMessage = new ChatMessage("알림", message.getMessage(), "");
        template.convertAndSend("/sub/chat", chatMessage);

        updateUserCnt(); // 참여자 갱신
    }

    @MessageMapping(value = "/messages") // 메시지 전송
    public void sendMessage(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        message.setCreatedDate(formattedDate);

        ChatResponseDto dto = new ChatResponseDto(message);

        template.convertAndSend("/sub/chat", message);
    }

    @PostMapping("/api/v1/filtering")
    public ResponseEntity<?> filterMessage(@RequestBody FilteringRequestDTO filteringRequestDTO) throws Exception {

        String message = filteringRequestDTO.getMessage();
        
        if (message.trim().isEmpty()){ // 공백만 가면
            return ResponseEntity.status(400).body("공백 문자열 감지");
        } else if (message.length() > 200) { // 200글자가 넘어가면
            return ResponseEntity.status(413).body("200자 초과 문자열 감지");
        }

        if(chatService.filteringChatMessage(message)) {
            return ResponseEntity.status(200).body("fail"); //욕설 필터에 걸림
        } else {
            return ResponseEntity.status(200).body("success"); // 정상
        }

    }

    @MessageMapping(value = "/exit") // 퇴장 메시지
    public void userExit(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        users.remove(message.getWriter());

        message.setWriter(message.getWriter());
        message.setMessage(message.getWriter()+"님이 퇴장하였습니다.");
        message.setCreatedDate(formattedDate);

        ChatMessage chatMessage = new ChatMessage("알림", message.getMessage(), "");
        template.convertAndSend("/sub/chat", chatMessage);
        updateUserCnt(); // 참여자 갱신

    }
}