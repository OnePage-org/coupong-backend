package com.onepage.coupong.service;


import java.util.Set;

public interface RedisZSetService {
    boolean addToZSet(String ZSetKey, String itemValue, double itemScore);
    Set<Object> getTopRankSet(String ZSetKey, int limit);
    Set<Object> getZSet(String ZSetKey);
    void removeItemFromZSet(String ZSetKey, String itemValue);
}
