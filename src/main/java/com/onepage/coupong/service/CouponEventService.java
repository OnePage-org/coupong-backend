package com.onepage.coupong.service;

import com.onepage.coupong.dto.UserRequestDto;
import com.onepage.coupong.entity.CouponEvent;
import com.onepage.coupong.entity.EventManager;
import com.onepage.coupong.entity.enums.CouponCategory;
import com.onepage.coupong.repository.CouponEventRepository;
import jdk.jfr.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CouponEventService {
    private final RedisQueueService redisQueueService;
    private final CouponEventRepository couponEventRepository;
    private EventManager eventManager;

    public void setEventManager(CouponCategory couponCategory, int couponCount, int endNums) {
        this.eventManager = new EventManager(couponCategory, couponCount, endNums);
    }

    public void setEventManager(CouponCategory couponCategory, int couponCount) {
        this.eventManager = new EventManager(couponCategory, couponCount);
    }

    public boolean addUserToQueue (UserRequestDto userRequestDto) {
        return redisQueueService.addToQueue(userRequestDto);
    }

    public void publishCoupons(int scheduleCount) {

        Set<Object> queue = redisQueueService.getTopRankSet(scheduleCount);

        System.out.println(queue.size() +" 큐 사이즈 !");

        for (Object userId : queue) {

            System.out.println("반목문 돌립니다 큐에서 유저 id 꺼내옴 " + userId);

            CouponEvent couponEvent = CouponEvent.builder().eventDate(LocalDateTime.now()).build();
            couponEventRepository.save(couponEvent);

            redisQueueService.removeUserFromQueue(userId);
            eventManager.decreaseCouponCount();
        }
    }

    public Set<Object> getQueue() {
        return redisQueueService.getSortedSet();
    }

    //쿠폰 발행된 사람들 데이터 RDB 영속
    private void persistEventAttemptData (CouponEvent couponEvent) {

    }

    public boolean validEnd() {
        return this.eventManager != null && this.eventManager.eventEnd();
    }
}
