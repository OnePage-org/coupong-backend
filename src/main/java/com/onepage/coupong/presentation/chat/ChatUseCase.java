package com.onepage.coupong.presentation.chat;

import com.onepage.coupong.business.chat.dto.ChatMessageDto;

public interface ChatUseCase {
    boolean filteringChatMessage(String chatMessage) throws Exception;
    ChatMessageDto userEnter(String username);
    ChatMessageDto userExit(String username);
    void sendMessage(ChatMessageDto message);
    void updateUserCnt();
    String getCurrentTime();
}
