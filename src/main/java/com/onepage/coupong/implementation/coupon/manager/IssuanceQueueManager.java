package com.onepage.coupong.implementation.coupon.manager;

import com.onepage.coupong.implementation.coupon.EventException;
import com.onepage.coupong.implementation.coupon.enums.EventExceptionType;
import com.onepage.coupong.infrastructure.redis.RedisZSetUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class IssuanceQueueManager implements RedisZSetUseCase {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String queueKeySeparator = "ISSUANCE QUEUE:";

    @Override
    public void addToZSet(String couponCategory, String userId, double attemptAt) {
        try {
            redisTemplate.opsForZSet().add(queueKeySeparator + couponCategory, userId, attemptAt);
        } catch (Exception e) {
            throw new EventException(EventExceptionType.EVENT_ATTEMPT_FAILED);
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
        return redisTemplate.opsForZSet().range(String.valueOf(queueKeySeparator + couponCategory), 0, limit - 1);
    }

    @Override
    public void removeItemFromZSet(String couponCategory, String itemValue) {
        redisTemplate.opsForZSet().remove(queueKeySeparator + couponCategory, itemValue);
    }

    public boolean isUserInQueue(String couponCategory, String userName) {
        try {
            Long rank = redisTemplate.opsForZSet().rank(queueKeySeparator + couponCategory, userName);
            return rank != null;
        } catch (Exception e) {
            return false;
        }
    }
}
