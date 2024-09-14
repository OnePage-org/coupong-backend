package com.onepage.coupong.service;

import com.onepage.coupong.dto.UserRequestDto;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class CouponEventService {

    private final CouponQueueService couponQueueService;

    public CouponEventService(CouponQueueService couponQueueService) {
        this.couponQueueService = couponQueueService;
    }

    public boolean addUserToQueue (UserRequestDto userRequestDto) {
        return couponQueueService.addToQueue(userRequestDto);
    }

    public void publishCoupons(int couponCount) {
        Set<Object> sortedUsers = couponQueueService.getSortedSet();

        int publishedCoupons = 0;
        for (Object userId : sortedUsers) {
            if (publishedCoupons >= couponCount) {
                break;
            }
            // 실제 쿠폰 발급 로직 수행 (DB 저장 등)
            // 쿠폰 발급 후 Redis에서 해당 유저 제거
            publishCouponForUser(userId);
            couponQueueService.removeUserFromQueue(userId);
            publishedCoupons++;
        }
    }

    private void publishCouponForUser(Object userId) {
        // 쿠폰 발행 성공 로직: DB 저장 등
    }
}
