package com.onepage.coupong.implementation.leaderboard;

import com.onepage.coupong.implementation.coupon.EventException;
import com.onepage.coupong.implementation.coupon.enums.EventExceptionType;
import com.onepage.coupong.infrastructure.redis.RedisZSetUseCase;
import com.onepage.coupong.presentation.leaderboard.LeaderboardUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardQueueManager implements RedisZSetUseCase {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LeaderboardUseCase leaderboardUsecase;
    private final String queueKeySeparator = "LEADERBOARD QUEUE:"; // 큐 키 구분자

    // 리더보드에 당첨자 추가
    @Override
    public void addToZSet(String couponCategory, String userId, double attemptAt) {
        log.info("Adding user to leaderboard ZSet: {}", userId);

        try {
            // Redis에 사용자 추가
            redisTemplate.opsForZSet().add(queueKeySeparator + couponCategory, userId, attemptAt);
            syncLeaderboardWithQueue(couponCategory, attemptAt); // 리더보드 동기화

        } catch (Exception e) {
            log.error("Error while adding to ZSet: ", e);
            throw new EventException(EventExceptionType.EVENT_REGISTER_LEADERBOARD_FAILED);
        }
    }

    @Override
    public Set<Object> getZSet(String couponCategory) {
        return redisTemplate.opsForZSet().range(queueKeySeparator + couponCategory, 0, -1); // 전체 사용자 가져오기
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> getTopRankSetWithScore(String couponCategory, int limit) {
        return redisTemplate.opsForZSet().rangeWithScores(queueKeySeparator + couponCategory, 0, limit - 1); // 상위 사용자 및 점수 가져오기
    }

    @Override
    public Set<Object> getTopRankSet(String couponCategory, int limit) {
        return redisTemplate.opsForZSet().range(queueKeySeparator + couponCategory, 0, limit - 1); // 상위 사용자 가져오기
    }

    @Override
    public void removeItemFromZSet(String couponCategory, String itemValue) {
        log.info("Removing user from queue: {}", itemValue);
        redisTemplate.opsForZSet().remove(queueKeySeparator + couponCategory, itemValue); // 사용자 제거
    }

    // 리더보드 큐와 동기화
    private void syncLeaderboardWithQueue(String couponCategory, Double attemptAt) {
        Set<Object> topWinners = getZSet(couponCategory);
        leaderboardUsecase.updateLeaderboard(couponCategory, topWinners, attemptAt); // 리더보드 업데이트
    }

    // 리더보드 초기화
    public void clearLeaderboardQueue(String couponCategory) {
        redisTemplate.opsForZSet().removeRange(queueKeySeparator + couponCategory, 0, -1); // 모든 사용자 제거
        syncLeaderboardWithQueue(couponCategory, null); // 리더보드 업데이트
    }

    // 리더보드안에 사용자가 있는지 조회
    public boolean isUserInQueue(String couponCategory, String userName) {
        try {
            // ZSet에서 유저 ID의 순위를 조회
            Long rank = redisTemplate.opsForZSet().rank(queueKeySeparator + couponCategory, userName);
            return rank != null; // 유저가 리더보드 큐에 있으면 true, 없으면 false 리턴
        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고 false 반환
            log.error("Error checking if user is in queue: ", e);
            return false;
        }
    }

}
