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

    //유저 요청 큐에 추가
    @Override
    public void addToZSet(String couponCategory, String userId, double attemptAt) {
        try {
            redisTemplate.opsForZSet().add(queueKeySeparator + couponCategory, userId, attemptAt);
        } catch (Exception e) {
            throw new EventException(EventExceptionType.EVENT_ATTEMPT_FAILED);
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

    // 대기열에 사용자가 있는지 조회
    public boolean isUserInQueue(String couponCategory, String userName) {
        try {
            // ZSet에서 유저 ID의 순위를 조회
            Long rank = redisTemplate.opsForZSet().rank(queueKeySeparator + couponCategory, userName);
            return rank != null; // 유저가 리더보드 큐에 있으면 true, 없으면 false 리턴
        } catch (Exception e) {
            return false;
        }
    }
}
