package com.onepage.coupong.global.scheduler;

import com.onepage.coupong.implementation.coupon.EventException;
import com.onepage.coupong.implementation.coupon.enums.EventExceptionType;
import com.onepage.coupong.jpa.coupon.CouponEvent;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import com.onepage.coupong.business.coupon.CouponEventService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponEventScheduler {

    private final ThreadPoolTaskScheduler taskScheduler;
    private Map<CouponCategory, ScheduledFuture<?>> scheduledTasks;  //카테고리 별 스케줄 관리

    private Long initialDelay;
    private Long eventDuration;

    @PostConstruct
    public void initializeMaps() {
        this.scheduledTasks = new HashMap<>();
    }

    public void scheduleEvent(CouponEvent event, CouponEventService couponEventService) {
        initializeEventTime(event);

        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            ScheduledFuture<?> publishTask = taskScheduler.scheduleWithFixedDelay(() -> {
                try {
                    if (!couponEventService.isEventInitialized(event.getCategory())) {
                        return;
                    }
                    if (couponEventService.validEnd(event.getCategory())) {
                        stopEvent(event.getCategory());
                        return;
                    }
                    couponEventService.publishCoupons(event.getCategory(), 10);
                } catch (Exception e) {
                    throw new EventException(EventExceptionType.EVENT_SCHEDULER_ERROR);
                }
            }, 5000);
            scheduledTasks.put(event.getCategory(), publishTask);  // 카테고리별 스케줄 관리
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

    private void initializeEventTime(CouponEvent event) {
        LocalDateTime eventStart = event.getDate();
        LocalDateTime eventEnd = event.getDate().plusMinutes(Long.parseLong(event.getDuration()));

        this.initialDelay = Duration.between(LocalDateTime.now(), eventStart).toMillis();
        this.eventDuration = Duration.between(eventStart, eventEnd).toMillis();

        validateEventTime();

        log.info("현재로부터 이벤트 시작 시간까지의 시간 차이: {}ms, 이벤트 진행 시간: {}ms", initialDelay, eventDuration);
    }

    public boolean isSchedulerStopped(CouponCategory category) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(category);
        return scheduledTask == null || scheduledTask.isCancelled();
    }

    private void validateEventTime() {
        if (initialDelay < 0) {
            log.warn("이벤트 시작 시간이 현재 시간보다 이전입니다. 스케줄링을 건너뜁니다.");
            throw new EventException(EventExceptionType.EVENT_REGISTER_SCHEDULER_FAILED);
        }
    }
}