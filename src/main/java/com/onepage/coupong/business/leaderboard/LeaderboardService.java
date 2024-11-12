package com.onepage.coupong.business.leaderboard;

import com.onepage.coupong.business.leaderboard.dto.CategoryDto;
import com.onepage.coupong.business.leaderboard.dto.LeaderboardDto;
import com.onepage.coupong.business.leaderboard.dto.SseEmitterDto;
import com.onepage.coupong.implementation.leaderboard.DtoMapper.LeaderboardDtoMapper;
import com.onepage.coupong.implementation.leaderboard.DtoMapper.SseDtoMapper;
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
    private final LeaderboardDtoMapper leaderboardDtoMapper;

    @Override
    public SseEmitterDto addSseEmitter() {
        emitterManager.validateAndRemoveEmitters();
        SseEmitter sseEmitter = emitterManager.addEmitter();
        return SseDtoMapper.toAddSseEmitter(sseEmitter);
    }

    @Override
    public LeaderboardDto getLeaderboard(String couponCategory) {
        Map<String, Map<Object, Double>> leaderboard = leaderboardQueueManager.getLeaderboard(couponCategory);
        return LeaderboardDtoMapper.toViewLeaderboard(leaderboard);
    }

//    @Override
//    public Map<String, Map<Object, Double>> getLeaderboard(String couponCategory) {
//        return leaderboardQueueManager.getLeaderboard(couponCategory);
//    }

    @Override
    public void clearLeaderboard(String couponCategory) {
        leaderboardQueueManager.clearLeaderboardQueue(couponCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<String> allCategories = leaderboardQueueManager.getAllCategories();
        return leaderboardDtoMapper.toGetAllCategories(allCategories);
    }

}
