package com.onepage.coupong.business.leaderboard;

import com.onepage.coupong.implementation.leaderboard.manager.EmitterManager;
import com.onepage.coupong.implementation.leaderboard.manager.LeaderboardQueueManager;
import com.onepage.coupong.presentation.leaderboard.LeaderboardUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService implements LeaderboardUseCase {

    private final EmitterManager emitterManager;
    private final LeaderboardQueueManager leaderboardQueueManager;

    @Override
    public SseEmitter addSseEmitter() {
        emitterManager.validateAndRemoveEmitters();
        return emitterManager.addEmitter();
    }

    @Override
    public Map<String, Map<Object, Double>> getLeaderboard(String couponCategory) {
        return leaderboardQueueManager.getLeaderboard(couponCategory);
    }

    @Override
    public void clearLeaderboard(String couponCategory) {
        leaderboardQueueManager.clearLeaderboardQueue(couponCategory);
    }

    @Override
    public List<String> getAllCategories() {
        return leaderboardQueueManager.getAllCategories();
    }

}
