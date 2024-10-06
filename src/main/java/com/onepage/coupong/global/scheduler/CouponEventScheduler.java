package com.onepage.coupong.global.scheduler;

import com.onepage.coupong.coupon.domain.CouponEvent;
import com.onepage.coupong.coupon.domain.enums.CouponCategory;
import com.onepage.coupong.coupon.service.CouponEventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponEventScheduler {

    private final ThreadPoolTaskScheduler taskScheduler;

    // 각 카테고리별 스케줄 관리
    private Map<CouponCategory, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleEvent(CouponEvent event, CouponEventService couponEventService) {
        log.info(event.getCategory() + " " + event.getCoupon_publish_nums());

        // 이벤트 시작 시간과 종료 시간을 계산
        LocalDateTime eventStart = event.getDate();
        LocalDateTime eventEnd = event.getDate().plusMinutes(Long.parseLong(event.getDuration()));

        long initialDelay = Duration.between(LocalDateTime.now(), eventStart).toMillis();
        long eventDuration = Duration.between(eventStart, eventEnd).toMillis();

        log.info("현재로부터 이벤트 시작 시간까지의 시간 차이: " + initialDelay + "ms, 이벤트 진행 시간: " + eventDuration + "ms");

        // 현재 시간이 이벤트 시작 시간 이후인지 체크
        if (initialDelay < 0) {
            log.warn("이벤트 시작 시간이 현재 시간보다 이전입니다. 스케줄링을 건너뜁니다.");
            return;  // 현재 시간이 이벤트 시작 시간보다 뒤이면 스케줄링하지 않음
        }

        // 이벤트 시작 시점에 스케줄링
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            log.info("이벤트 시작: 카테고리 = {}, 쿠폰 수 = {}", event.getCategory(), event.getCoupon_publish_nums());
            couponEventService.startEvent(event);

            // 이벤트 종료 전까지 일정 간격으로 쿠폰 발행
            ScheduledFuture<?> publishTask = taskScheduler.scheduleWithFixedDelay(() -> {
                try {
                    if (!couponEventService.isEventInitialized(event.getCategory())) {
                        log.info("이벤트가 초기화되지 않았습니다.");
                        return;
                    }
                    if (couponEventService.validEnd(event.getCategory())) {
                        log.info("이벤트 종료: 쿠폰 발행 가능 개수 충족");
                        stopEvent(event.getCategory());
                        return;
                    }

                    couponEventService.publishCoupons(event.getCategory(), 10);
                    log.info("쿠폰 발행 시도 완료");
                } catch (Exception e) {
                    log.error("스케줄러 오류 발생", e);
                }
            }, 3000);

            // 카테고리별로 스케줄 관리
            scheduledTasks.put(event.getCategory(), publishTask);

        }, new Date(System.currentTimeMillis() + initialDelay));

        // 종료 시점에 스케줄러 중단
        taskScheduler.schedule(() -> stopEvent(event.getCategory()), new Date(System.currentTimeMillis() + initialDelay + eventDuration));
    }

    // 카테고리별로 이벤트 스케줄 중단
    public void stopEvent(CouponCategory category) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(category);
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
            log.info("이벤트 스케줄러 중단: 카테고리 = {}", category);
            scheduledTasks.remove(category);
        }
    }
}