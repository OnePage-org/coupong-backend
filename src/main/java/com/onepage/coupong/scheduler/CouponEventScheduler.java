package com.onepage.coupong.scheduler;

import com.onepage.coupong.entity.CouponEvent;
import com.onepage.coupong.service.CouponEventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponEventScheduler {

    private final ThreadPoolTaskScheduler taskScheduler;

    @Getter
    private ScheduledFuture<?> scheduledTask;

    public void scheduleEvent(CouponEvent event, CouponEventService couponEventService) {
        log.info(event.getCategory()+" "+
                event.getCoupon_publish_nums()+" "+
                event.getCoupon_publish_nums());
        // 스케줄 등록 전에 이벤트를 먼저 초기화
        couponEventService.initializeEvent(
                event.getCategory(),
                event.getCoupon_publish_nums(),
                0
        );

        // 이벤트 시작 시간과 종료 시간을 계산
        LocalDateTime eventStart = event.getDate();
        LocalDateTime eventEnd = event.getDate().plusMinutes(Long.parseLong(event.getDuration()));

        long initialDelay = Duration.between(LocalDateTime.now(), eventStart).toMillis();
        long eventDuration = Duration.between(eventStart, eventEnd).toMillis();

        // 스케줄링
        scheduledTask = taskScheduler.scheduleWithFixedDelay(() -> {
            try {
                if (!couponEventService.isEventInitialized()) {
                    log.info("이벤트가 초기화되지 않았습니다.");
                    return;
                }
                if (couponEventService.validEnd()) {
                    log.info("이벤트 종료: 쿠폰 발행 가능 개수 충족");
                    stopEvent();
                    return;
                }

                couponEventService.publishCoupons(10);
                log.info("쿠폰 발행 시도 완료");
            } catch (Exception e) {
                log.error("스케줄러 오류 발생", e);
            }
        }, 10000);  // 10초마다 실행

        taskScheduler.schedule(() -> log.info("이벤트 스케줄러 시작"), new Date(System.currentTimeMillis() + initialDelay));
        taskScheduler.schedule(this::stopEvent, new Date(System.currentTimeMillis() + initialDelay + eventDuration));
    }

    public void stopEvent() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
            log.info("이벤트 스케줄러 중단");
        }
    }

}