package com.onepage.coupong.scheduler;

import com.onepage.coupong.service.CouponEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class EventScheduler {

    private final CouponEventService couponEventService;

    @Scheduled(fixedRate = 1000)
    public void couponEventScheduler() {
        if(couponEventService.validEnd()) {
            log.info("이벤트 쿠폰 발행 가능 개수 충족");
            return;
        }
        couponEventService.publishCoupons(10);
        log.info("쿠폰 발행 완료");
    }
}