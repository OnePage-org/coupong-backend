package com.onepage.coupong.business.chat;

import com.onepage.coupong.business.chat.dto.ChatMessageDto;
import com.onepage.coupong.global.banwordFilter.PatternFiltering;
import com.onepage.coupong.global.banwordFilter.WordListLoader;
import com.onepage.coupong.presentation.chat.ChatUseCase;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService implements ChatUseCase {

    private final PatternFiltering patternFiltering;
    private final Map<String, Boolean> users = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate template;

    enum WordType {
        banWord,
        allowWord
    }

    public ChatService(SimpMessagingTemplate template) throws IOException {
        this.template = template;
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
        return patternFiltering.checkBanWord(chatMessage);
    }

    public ChatMessageDto userEnter(String username) {
        users.put(username, Boolean.TRUE);
        updateUserCnt();
        return new ChatMessageDto("입장", username+"님이 입장하였습니다.", "");
    }

    public ChatMessageDto userExit(String username) {
        users.remove(username);
        updateUserCnt();
        return new ChatMessageDto("퇴장", username+"님이 퇴장하였습니다.", "");
    }

    public void sendMessage(ChatMessageDto message) {
        if (!(message.getMessage().contains("입장") || message.getMessage().contains("퇴장"))) message.setCreatedDate(getCurrentTime());
        template.convertAndSend("/sub/chat", message);
    }

    public void updateUserCnt() {
        template.convertAndSend("/sub/total", users);
        template.convertAndSend("/sub/users", users.size());
    }

    public String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a", Locale.ENGLISH);
        return LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);
    }
}
