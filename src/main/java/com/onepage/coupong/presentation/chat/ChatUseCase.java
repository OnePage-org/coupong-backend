package com.onepage.coupong.presentation.chat;

import com.onepage.coupong.business.chat.dto.ChatMessageDto;

public interface ChatUseCase {
    // 메시지 내에서 금칙어 확인
    boolean filteringChatMessage(String chatMessage) throws Exception;
    // 사용자 입장
    ChatMessageDto userEnter(String username);
    // 사용자 퇴장
    ChatMessageDto userExit(String username);
    // 메시지 전송
    void sendMessage(ChatMessageDto message);
    // 사용자 수 및 목록 업데이트
    void updateUserCnt();
}
