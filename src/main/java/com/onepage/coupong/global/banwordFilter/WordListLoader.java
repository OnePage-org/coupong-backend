package com.onepage.coupong.global.banwordFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordListLoader {
    public static List<String> loadWords(String wordType) throws IOException {
        List<String> words = new ArrayList<>();
        try (InputStream inputStream = WordListLoader.class.getClassLoader().getResourceAsStream(wordType+"s.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        }
        return words;
    }
}