package com.onepage.coupong.global.banwordFilter;

import java.util.*;

public class PatternFiltering {
    //    private final Set<String> banWordsSet = new HashSet<>();
//    private final Set<String> allowWordsSet = new HashSet<>();
    private final AhoCorasick ahoCorasick = new AhoCorasick();
    private List<int[]> outputWordPositionData;
    private List<String> outputWords;

    public void addWords(String wordType, Collection<String> words) {
        /*
        if(wordType.equals("banWord")) {
            banWordsSet.addAll(banWords);
        } else {
            allowWordsSet.addAll(banWords);
        }
         */
        for (String word : words) {
            ahoCorasick.addKeyword(wordType, word);
        }
        ahoCorasick.buildFailureLinks();
    }

    // 문자열에서 특정 문자 제거
    private String preprocessInput(String input) {
        String patternText = "[\\s,.!?@1234567890]*";
        return input.replaceAll(patternText, "");
    }

    public boolean checkBanWord(String input) {
        outputWordPositionData = new ArrayList<>();
        outputWords = new ArrayList<>();

        String preprocessedInput = preprocessInput(input);
        ahoCorasick.search(preprocessedInput,outputWordPositionData, outputWords);

        //System.out.println(outputWords);

        return !filterAllowWord().isEmpty();
    }

    public HashSet<String> filterAllowWord() {
        HashSet<String> banWords = new HashSet<>();
        Queue<int[]> queue = new PriorityQueue<>((o1, o2) -> {
            //시작위치가 같은 경우에는 허용어가 먼저 뽑혀서 그 아래 깔리는 비속어를 잡아 먹어야함
            if (o1[2] == o2[2]) {
                return Integer.compare(o1[1], o2[1]);
            }
            return Integer.compare(o1[2], o2[2]);
        });
        queue.addAll(outputWordPositionData);

        int allowWordLastPosition = -1;

        while (!queue.isEmpty()) {
            int[] outputWordPositionData = queue.poll();
            int wordStartPosition = outputWordPositionData[2];
            int wordEndPosition = outputWordPositionData[3];
            boolean isBanWord = outputWordPositionData[1]==1;


            //비속어임
            if (isBanWord) {
                if(allowWordLastPosition < wordStartPosition) {
                    banWords.add(outputWords.get(outputWordPositionData[0]));
                    /*System.out.println(outputWords.get(outputWordPositionData[0]));
                    System.out.println("allowWordLastPosition : "+allowWordLastPosition +"  "+
                            "wordStartPosition: " + wordStartPosition + " wordEndPosition: " + wordEndPosition );*/
                }
                //허용어임
            } else {
                //허용어의 범위 기록
                allowWordLastPosition = Math.max(allowWordLastPosition, wordEndPosition);
            }
        }

        //banWords.addAll(outputWords);
        return banWords;
    }
}

