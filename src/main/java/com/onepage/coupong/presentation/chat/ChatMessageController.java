package com.onepage.coupong.presentation.chat;

import com.onepage.coupong.business.chat.dto.FilteringRequestDto;
import com.onepage.coupong.business.chat.dto.ChatMessageDto;
import com.onepage.coupong.global.presentation.CommonResponseEntity;
import com.onepage.coupong.implementation.chat.enums.ChatExceptionType;
import com.onepage.coupong.presentation.chat.enums.FilteringControllerResp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

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
    @ResponseBody
    public CommonResponseEntity<?> filterMessage(@RequestBody FilteringRequestDto filteringRequestDTO) throws Exception {

        String message = filteringRequestDTO.getMessage();

        if (message.trim().isEmpty()){
            return ChatExceptionType.MESSAGE_BLANK.toResponseErrorEntity();
        } else if (message.length() > 200) {
            return ChatExceptionType.MESSAGE_TOO_LONG.toResponseErrorEntity();
        }

        if(chatUseCase.filteringChatMessage(message)) {
            System.out.println(CommonResponseEntity.success(FilteringControllerResp.BAN_WORD));
            return CommonResponseEntity.success(FilteringControllerResp.BAN_WORD);
        } else {
            return CommonResponseEntity.success(FilteringControllerResp.ALLOW_WORD);
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