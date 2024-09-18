package com.onepage.coupong.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LeaderBoardQueueService  implements  RedisZSetService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String queueKeySeparator = "LEADERBOARD QUEUE:";

    public LeaderBoardQueueService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean addToZSet(String couponCategory, String userId, double attemptAt) {
        try {
            redisTemplate.opsForZSet().add(queueKeySeparator + couponCategory, userId, attemptAt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<Object> getZSet(String couponCategory) {
        return redisTemplate.opsForZSet().range(queueKeySeparator + couponCategory, 0, -1);
    }

    @Override
    public Set<Object> getTopRankSet(String couponCategory, int limit) {
        return redisTemplate.opsForZSet().range(String.valueOf(queueKeySeparator + couponCategory), 0, limit - 1);
    }

    @Override
    public void removeItemFromZSet(String couponCategory, String itemValue) {
        System.out.println("삭제합니다 유저 큐에서 " + itemValue);
        redisTemplate.opsForZSet().remove(queueKeySeparator + couponCategory, itemValue);
    }
}
