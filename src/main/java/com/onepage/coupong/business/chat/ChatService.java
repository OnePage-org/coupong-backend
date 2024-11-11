package com.onepage.coupong.business.chat;

import com.onepage.coupong.business.chat.dto.ChatMessageDto;
import com.onepage.coupong.global.banwordFilter.PatternFiltering;
import com.onepage.coupong.global.banwordFilter.WordListLoader;
import com.onepage.coupong.implementation.chat.ChatFilterException;
import com.onepage.coupong.implementation.chat.enums.ChatExceptionType;
import com.onepage.coupong.implementation.chat.manager.ChatMessageManager;
import com.onepage.coupong.presentation.chat.ChatUseCase;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService implements ChatUseCase {

    private final PatternFiltering patternFiltering;
    private final Map<String, Boolean> users = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate template;
    private final ChatMessageManager chatMessageManager;

    enum WordType {
        banWord,
        allowWord
    }

    public ChatService(SimpMessagingTemplate template, ChatMessageManager chatMessageManager) throws IOException {
        this.template = template;
        this.chatMessageManager = chatMessageManager;
        this.patternFiltering = new PatternFiltering();
        initializeBanWords();
    }

    private void initializeBanWords() throws IOException {
        List<String> banWords = WordListLoader.loadWords(String.valueOf(WordType.banWord));
        List<String> allowWords = WordListLoader.loadWords(String.valueOf(WordType.allowWord));

        patternFiltering.addWords(String.valueOf(WordType.banWord), banWords);
        patternFiltering.addWords(String.valueOf(WordType.allowWord), allowWords);
    }

    public boolean filteringChatMessage(String chatMessage) {
        if (chatMessage.trim().isEmpty()) {
            throw new ChatFilterException(ChatExceptionType.MESSAGE_BLANK);
        }
        if (chatMessage.length() > 200) {
            throw new ChatFilterException(ChatExceptionType.MESSAGE_TOO_LONG);
        }

        return patternFiltering.checkBanWord(chatMessage);
    }

    public ChatMessageDto userEnter(String username) {
        users.put(username, Boolean.TRUE);
        updateUserCnt();
        ChatMessageDto message = chatMessageManager.createEnterMessage(username);
        return chatMessageManager.createChatMessage(message.getMessage(), username, message.getType());
    }

    public ChatMessageDto userExit(String username) {
        users.remove(username);
        updateUserCnt();
        ChatMessageDto message = chatMessageManager.createExitMessage(username);
        return chatMessageManager.createChatMessage(message.getMessage(), username, message.getType());
    }

    public void sendMessage(ChatMessageDto message) {
        message = chatMessageManager.createChatMessage(message.getMessage(), message.getWriter(), message.getType());
        template.convertAndSend("/sub/chat", message);
    }

    public void updateUserCnt() {
        template.convertAndSend("/sub/total", users);
        template.convertAndSend("/sub/users", users.size());
    }

}
