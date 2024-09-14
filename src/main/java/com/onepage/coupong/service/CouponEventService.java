package com.onepage.coupong.service;

import com.onepage.coupong.dto.UserRequestDto;
import com.onepage.coupong.entity.Coupon;
import com.onepage.coupong.entity.CouponEvent;
import com.onepage.coupong.entity.User;
import com.onepage.coupong.entity.enums.CouponEventState;
import com.onepage.coupong.repository.CouponEventRepository;
import com.onepage.coupong.sign.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Service
public class CouponEventService {

    private final CouponQueueService couponQueueService;
    private final CouponEventRepository couponEventRepository;
    private final UserRepository userRepository;

    public CouponEventService(CouponQueueService couponQueueService, CouponEventRepository couponEventRepository, UserRepository userRepository) {
        this.couponQueueService = couponQueueService;
        this.couponEventRepository = couponEventRepository;
        this.userRepository = userRepository;
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
        CouponEvent couponEvent;
        User user = userRepository.getById((Long) userId);
        couponEvent = new CouponEvent();

        couponEventRepository.save(couponEvent);
    }
}
