package com.onepage.coupong.infrastructure.redis;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public interface RedisZSetService {
    boolean addToZSet(String ZSetKey, String itemValue, double itemScore);
    Set<Object> getTopRankSet(String ZSetKey, int limit);
    Set<Object> getZSet(String ZSetKey);
    //Map<Object, Set<Object>> getZSetWithKey(String ZSetKey);
    Set<ZSetOperations.TypedTuple<Object>> getTopRankSetWithScore(String ZSetKey, int limit);
    void removeItemFromZSet(String ZSetKey, String itemValue);
}
