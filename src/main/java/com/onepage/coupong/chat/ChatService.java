package com.onepage.coupong.chat;

import com.onepage.coupong.filtering.PatternFiltering;
import com.onepage.coupong.filtering.WordListLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ChatService {

    private final PatternFiltering patternFiltering;

    enum WordType {
        banWord,
        allowWord
    }

    public ChatService() throws IOException {
        patternFiltering = new PatternFiltering();
        initializeBanWords();
    }

    private void initializeBanWords() throws IOException {
        List<String> banWords = WordListLoader.loadWords(String.valueOf(WordType.banWord));
        List<String> allowWords = WordListLoader.loadWords(String.valueOf(WordType.allowWord));

        patternFiltering.addWords(String.valueOf(WordType.banWord), banWords);
        patternFiltering.addWords(String.valueOf(WordType.allowWord), allowWords);
    }

    public boolean filteringChatMessage(String chatMessage) throws Exception {
        return  patternFiltering.checkBanWord(chatMessage);
    }
}
