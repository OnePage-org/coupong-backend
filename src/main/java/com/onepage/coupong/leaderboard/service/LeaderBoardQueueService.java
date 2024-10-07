package com.onepage.coupong.leaderboard.service;

import com.onepage.coupong.infrastructure.redis.RedisZSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class LeaderBoardQueueService implements RedisZSetService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LeaderboardService leaderboardService;

    private final String queueKeySeparator = "LEADERBOARD QUEUE:";

    @Autowired
    public LeaderBoardQueueService(RedisTemplate<String, Object> redisTemplate, LeaderboardService leaderboardService) {
        this.redisTemplate = redisTemplate;
        this.leaderboardService = leaderboardService;
    }

    // 리더보드에 당첨자 추가
    @Override
    public boolean addToZSet(String couponCategory, String userId, double attemptAt) {
        log.info("Adding user to leaderboard ZSet: {}", userId);

        try {
            // Redis의 add 메서드 호출
            redisTemplate.opsForZSet().add(queueKeySeparator + couponCategory, userId, attemptAt);

            // 리더보드 정보 가져오기 및 업데이트
            syncLeaderboardWithQueue(couponCategory); // 이름 변경

            return true; // 추가 성공
        } catch (Exception e) {
            log.error("Error while adding to ZSet: ", e);
            return false; // 예외 발생 시 false 반환
        }
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

    // 리더보드 큐와 동기화
    private void syncLeaderboardWithQueue(String couponCategory) {
        Set<Object> topWinners = getZSet(couponCategory);
        leaderboardService.updateLeaderboard(couponCategory, topWinners); // 한 번에 처리
    }

    // 리더보드 초기화
    public void clearLeaderboardQueue(String couponCategory) {
        redisTemplate.opsForZSet().removeRange(queueKeySeparator + couponCategory, 0, -1);
        syncLeaderboardWithQueue(couponCategory); // 클리어 후 리더보드 업데이트
    }
}
