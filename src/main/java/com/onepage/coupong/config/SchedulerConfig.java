package com.onepage.coupong.config;

import com.onepage.coupong.service.CouponEventService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final CouponEventService couponEventService;

    public SchedulerConfig(CouponEventService couponEventService) {
        this.couponEventService = couponEventService;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void scheduleCouponEvent() {
        couponEventService.publishCoupons(30);
    }
}
