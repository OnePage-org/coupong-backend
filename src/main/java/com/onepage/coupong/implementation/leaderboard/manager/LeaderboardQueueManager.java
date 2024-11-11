package com.onepage.coupong.implementation.leaderboard.manager;

import com.onepage.coupong.implementation.leaderboard.LeaderboardQueueException;
import com.onepage.coupong.implementation.leaderboard.enums.LeaderboardQueueExceptionType;
import com.onepage.coupong.infrastructure.redis.RedisZSetUseCase;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardQueueManager implements RedisZSetUseCase {

    private final RedisTemplate<String, Object> redisTemplate;
    private final EmitterManager emitterManager;
    private final String queueKeySeparator = "LEADERBOARD QUEUE:";

    @Override
    public void addToZSet(String couponCategory, String userId, double attemptAt) {

        try {
            redisTemplate.opsForZSet().add(queueKeySeparator + couponCategory, userId, attemptAt);
            updateLeaderboard(couponCategory, attemptAt);
        } catch (Exception e) {
            throw new LeaderboardQueueException(LeaderboardQueueExceptionType.REDIS_ZSET_ADD_FAILED);
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
        redisTemplate.opsForZSet().remove(queueKeySeparator + couponCategory, itemValue); // 사용자 제거
    }

    public void clearLeaderboardQueue(String couponCategory) {
        redisTemplate.opsForZSet().removeRange(queueKeySeparator + couponCategory, 0, -1); // 모든 사용자 제거
        updateLeaderboard(couponCategory, null);
    }

    public boolean isUserInQueue(String couponCategory, String userName) {
        try {
            Long rank = redisTemplate.opsForZSet().rank(queueKeySeparator + couponCategory, userName);
            return rank != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateLeaderboard(String couponCategory, Double entryTime) {
        Set<Object> topWinners = getZSet(couponCategory);
        String message = createMessageForLeaderboardUpdate(couponCategory, topWinners, entryTime);
        emitterManager.sendMessageToEmitters(message);
    }

    public String createMessageForLeaderboardUpdate(String couponCategory, Set<Object> topWinners, double entryTime) {
        List<String> winners = topWinners.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        String winnersJson = winners.isEmpty() ? "[]" : String.format("[\"%s\"]", String.join("\",\"", winners));
        String message = String.format("{\"couponCategory\": \"%s\", \"winners\": %s, \"entryTime\": %f}",
                couponCategory, winnersJson, entryTime);

        return message;
    }

    public Map<String, Map<Object, Double>> getLeaderboard(String couponCategory) {
        Set<ZSetOperations.TypedTuple<Object>> topWinners = getTopRankSetWithScore(couponCategory, 0);
        Map<Object, Double> winnersWithScores = new HashMap<>();

        if (topWinners != null) {
            for (ZSetOperations.TypedTuple<Object> tuple : topWinners) {
                winnersWithScores.put(tuple.getValue(), tuple.getScore());
            }
        }
        return Map.of(couponCategory, winnersWithScores);
    }

    public List<String> getAllCategories() {
        return Arrays.stream(CouponCategory.values())
                .filter(category -> category != CouponCategory.DEFAULT)
                .map(Enum::name)
                .toList();
    }
}
