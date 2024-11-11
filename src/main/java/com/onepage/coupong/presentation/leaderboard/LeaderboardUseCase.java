package com.onepage.coupong.presentation.leaderboard;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface LeaderboardUseCase {

    SseEmitter addSseEmitter();

    Map<String, Map<Object, Double>> getLeaderboard(String couponCategory);

    void clearLeaderboard(String couponCategory);

    List<String> getAllCategories();

}
