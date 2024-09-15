package com.onepage.coupong.service;

import com.onepage.coupong.dto.UserRequestDto;
import com.onepage.coupong.entity.enums.CouponCategory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisQueueService {

    private final RedisTemplate<String, Object> redisTemplate;
    private String queueKey = "default_key";

    public RedisQueueService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //유저 요청 큐에 추가
    public boolean addToQueue(UserRequestDto userRequestDto) {
        try {
            queueKey = String.valueOf(userRequestDto.getCouponCategory());
            redisTemplate.opsForZSet().add(queueKey, userRequestDto.getId(), userRequestDto.getAttemptAt());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 유저 요청 목록(대기열) 가져오기
    public Set<Object> getSortedSet() {
        return redisTemplate.opsForZSet().range(queueKey, 0, -1);
    }

    public Set<Object> getTopRankSet(int end) {
        return redisTemplate.opsForZSet().range(String.valueOf(queueKey), 0, end-1);
    }

    //쿠폰 발행 후 큐에서 제거
    public void removeUserFromQueue(Object userId) {
        System.out.println("삭제합니다 유저 큐에서 " + userId);
        redisTemplate.opsForZSet().remove(queueKey, userId);
    }
}
