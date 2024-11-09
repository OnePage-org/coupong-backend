package com.onepage.coupong.presentation.chat;

import com.onepage.coupong.business.chat.dto.FilteringRequestDto;
import com.onepage.coupong.business.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ChatUseCase chatUseCase;

    private final SimpMessagingTemplate template;
    private final Map<String, Boolean> users = new ConcurrentHashMap<>(); // 참여자 Map -> 동시성을 위한 ConcurrentHashMap사용

    public void updateUserCnt() { // 참여자 수 갱신
        int userCnt = users.size();
        template.convertAndSend("/sub/total", users);
        template.convertAndSend("/sub/users", userCnt);
    }
    @MessageMapping(value = "/enter") // 입장 메시지
    public void userEnter(ChatMessageDto message, StompHeaderAccessor headerAccessor) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        message.setMessage(message.getWriter()+"님이 입장하였습니다.");
        message.setCreatedDate(formattedDate);

        // username을 WebSocket 세션에 저장
        headerAccessor.getSessionAttributes().put("username", message.getWriter());

        users.put(message.getWriter(), Boolean.TRUE);

        ChatMessageDto chatMessageDTO = new ChatMessageDto("입장", message.getMessage(), "");
        template.convertAndSend("/sub/chat", chatMessageDTO);

        updateUserCnt(); // 참여자 갱신
    }

    @MessageMapping(value = "/messages") // 메시지 전송
    public void sendMessage(ChatMessageDto message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH); // 시간 format
        String formattedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);

        message.setCreatedDate(formattedDate);

        template.convertAndSend("/sub/chat", message);
    }

    @PostMapping("/api/v1/filtering")
    public ResponseEntity<?> filterMessage(@RequestBody FilteringRequestDto filteringRequestDTO) throws Exception {

        String message = filteringRequestDTO.getMessage();

        if (message.trim().isEmpty()){ // 공백만 가면
            return ResponseEntity.status(400).body("공백 문자열 감지");
        } else if (message.length() > 200) { // 200글자가 넘어가면
            return ResponseEntity.status(413).body("200자 초과 문자열 감지");
        }

        if(chatUseCase.filteringChatMessage(message)) {
            return ResponseEntity.status(200).body("fail"); //욕설 필터에 걸림
        } else {
            return ResponseEntity.status(200).body("success"); // 정상
        }

    }

    @EventListener // 퇴장 메시지 - 세션으로 처리
    public void userExit(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) accessor.getSessionAttributes().get("username");

        if (username != null) {
            users.remove(username);
            ChatMessageDto message = new ChatMessageDto("퇴장", username + "님이 퇴장하였습니다.", "");
            template.convertAndSend("/sub/chat", message);
            updateUserCnt(); // 참여자 갱신
        }
    }

    public void clearUserList() {
        users.clear(); // 사용자 목록 초기화
        updateUserCnt();
    }

    @EventListener(ApplicationReadyEvent.class) //Spring Boot 애플리케이션이 모든 초기화 작업을 마친 후에 발생하는 이벤트
    public void onApplicationStart() {
        clearUserList();  // 서버 시작 시 사용자 목록 초기화

        // WebSocket 강제 종료 로직 추가
        template.convertAndSend("/sub/exit", "서버 재시작");
    }
}