package com.onepage.coupong.service;

import com.onepage.coupong.dto.UserRequestDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class CouponQueueService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String COUPON_QUEUE_KEY = "couponQueue";

    public CouponQueueService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //유저 요청 큐에 추가
    public boolean addToQueue(UserRequestDto userRequestDto) {
        try {
            redisTemplate.opsForZSet().add(COUPON_QUEUE_KEY, userRequestDto.getId(), System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 유저 요청 목록 가져오기
    public Set<Object> getSortedSet() {
        return redisTemplate.opsForZSet().range(COUPON_QUEUE_KEY, 0, -1);
    }

    //쿠폰 발행 후 큐에서 제거
    public void removeUserFromQueue(Object userId) {
        redisTemplate.opsForZSet().remove(COUPON_QUEUE_KEY, userId);
    }
}
