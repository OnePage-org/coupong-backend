package com.onepage.coupong.service;

import com.onepage.coupong.dto.UserRequestDto;
import com.onepage.coupong.entity.CouponEvent;
import com.onepage.coupong.entity.EventManager;
import com.onepage.coupong.entity.User;
import com.onepage.coupong.entity.enums.CouponCategory;
import com.onepage.coupong.repository.CouponEventRepository;
import com.onepage.coupong.sign.repository.UserRepository;
import jdk.jfr.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CouponEventService {
    private final RedisQueueService redisQueueService;
    private final CouponEventRepository couponEventRepository;
    private EventManager eventManager;

    public void setEventManager(EventType eventType, int couponCount, int endNums) {
        this.eventManager = new EventManager(eventType, couponCount, endNums);
    }

    public void setEventManager(EventType eventType, int couponCount) {
        this.eventManager = new EventManager(eventType, couponCount);
    }

    public boolean addUserToQueue (UserRequestDto userRequestDto) {
        return redisQueueService.addToQueue(userRequestDto);
    }

    public void publishCoupons(int scheduleCount) {

        Set<Object> queue = redisQueueService.getTopRankSet(scheduleCount);

        for (Object userId : queue) {
            CouponEvent couponEvent = (CouponEvent) userId;
            couponEventRepository.save(couponEvent);
            redisQueueService.removeUserFromQueue(userId);
            eventManager.decreaseCouponCount();
        }
    }

    //쿠폰 발행된 사람들 데이터 RDB 영속
    private void persistEventAttemptData (CouponEvent couponEvent) {

    }

    public boolean validEnd() {
        return this.eventManager != null && this.eventManager.eventEnd();
    }
}
