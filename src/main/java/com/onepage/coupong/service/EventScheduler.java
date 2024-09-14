package com.onepage.coupong.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final CouponEventService couponEventService;

    @Scheduled(fixedRate = 1000)
    public void couponEventScheduler() {
        couponEventService.publishCoupons(10);
    }
}
