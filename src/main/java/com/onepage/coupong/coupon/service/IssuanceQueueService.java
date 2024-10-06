package com.onepage.coupong.coupon.service;

import com.onepage.coupong.infrastructure.redis.RedisZSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class IssuanceQueueService implements RedisZSetService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String queueKeySeparator = "ISSUANCE QUEUE:";


    public IssuanceQueueService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //유저 요청 큐에 추가
    @Override
    public boolean addToZSet(String couponCategory, String userId, double attemptAt) {
        try {
            redisTemplate.opsForZSet().add(queueKeySeparator + couponCategory, userId, attemptAt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 유저 요청 목록(대기열) 가져오기
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
        return redisTemplate.opsForZSet().range(String.valueOf(queueKeySeparator + couponCategory), 0, limit - 1);
    }

    //쿠폰 발행 후 큐에서 제거
    @Override
    public void removeItemFromZSet(String couponCategory, String itemValue) {
        log.info("삭제합니다 유저 큐에서 " + itemValue);
        redisTemplate.opsForZSet().remove(queueKeySeparator + couponCategory, itemValue);
    }
}
