package com.onepage.coupong.leaderboard.service;

import com.onepage.coupong.leaderboard.dto.LeaderboardUpdateResponseDto;
import com.onepage.coupong.infrastructure.redis.RedisZSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LeaderBoardQueueService implements RedisZSetService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LeaderboardService leaderboardService; // 리더보드 서비스 주입

    private final String queueKeySeparator = "LEADERBOARD QUEUE:";

    @Autowired
    public LeaderBoardQueueService(RedisTemplate<String, Object> redisTemplate, LeaderboardService leaderboardService) {
        this.redisTemplate = redisTemplate;
        this.leaderboardService = leaderboardService;
    }

    @Override
    public boolean addToZSet(String couponCategory, String userId, double attemptAt) {
        log.info("Adding user to leaderboard ZSet: {}", userId);

        boolean isAdded = false;
        try {
            isAdded = Boolean.TRUE.equals(redisTemplate.opsForZSet().add(queueKeySeparator + couponCategory, userId, attemptAt));
            if (isAdded) {
                // 리더보드 정보 가져오기
                Set<Object> topWinners = getZSet(couponCategory);
                List<String> winnerList = topWinners.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());

                // DTO 생성
                LeaderboardUpdateResponseDto updateDTO = new LeaderboardUpdateResponseDto(couponCategory, winnerList);

                // 리더보드 업데이트 (sink를 통해 데이터 발행)
                leaderboardService.updateLeaderboard(updateDTO); // Flux로 전송

            }
        } catch (Exception e) {
            log.error("Error " +
                    "while adding to ZSet: ", e);
        }
        return isAdded;
    }

    @Override
    public Set<Object> getZSet(String couponCategory) {
        return redisTemplate.opsForZSet().range(queueKeySeparator + couponCategory, 0, -1);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> getTopRankSetWithScore(String couponCategory, int limit) {
        return redisTemplate.opsForZSet().rangeWithScores(queueKeySeparator + couponCategory, 0, limit - 1);
    }

    @Override
    public Set<Object> getTopRankSet(String couponCategory, int limit) {
        return redisTemplate.opsForZSet().range(queueKeySeparator + couponCategory, 0, limit - 1);
    }

    @Override
    public void removeItemFromZSet(String couponCategory, String itemValue) {
        log.info("Removing user from queue: {}", itemValue);
        redisTemplate.opsForZSet().remove(queueKeySeparator + couponCategory, itemValue);
    }
    public void clearLeaderboardQueue(String couponCategory) {
        redisTemplate.opsForZSet().removeRange(queueKeySeparator + couponCategory, 0, -1);
    }
}