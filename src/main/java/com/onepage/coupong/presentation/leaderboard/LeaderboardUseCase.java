package com.onepage.coupong.presentation.leaderboard;

import com.onepage.coupong.business.leaderboard.dto.CategoryDto;
import com.onepage.coupong.business.leaderboard.dto.LeaderboardDto;
import com.onepage.coupong.business.leaderboard.dto.SseEmitterDto;

import java.util.List;
import java.util.Map;

public interface LeaderboardUseCase {

    SseEmitterDto addSseEmitter();

    LeaderboardDto getLeaderboard(String couponCategory);
//Map<String, Map<Object, Double>> getLeaderboard(String couponCategory);

    void clearLeaderboard(String couponCategory);

    List<CategoryDto> getAllCategories();

}
