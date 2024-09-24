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

        // 이벤트 시작 시간에 맞춰 스케줄링 설정
        taskScheduler.schedule(() -> {
            log.info("이벤트가 시작되었습니다: 카테고리 = {}, 쿠폰 수 = {}", event.getCategory(), event.getCoupon_publish_nums());

            // 이벤트 초기화
            couponEventService.initializeEvent(event.getCategory(), event.getCoupon_publish_nums(), 0);

            //레디스 대기열에 뭔가 대기자가 남아있으면 안됨 비워줘야해 !!!!

            // 이벤트 종료 전까지 10초 간격으로 쿠폰 발행 시도
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
            }, 10000);  // 10초마다 실행 //테스트 위해 1초로 수정

        }, new Date(System.currentTimeMillis() + initialDelay));  // 이벤트 시작 시간에 맞춰 스케줄 시작

        // 이벤트 종료 시간에 스케줄러 중단 설정
        taskScheduler.schedule(this::stopEvent, new Date(System.currentTimeMillis() + initialDelay + eventDuration));
    }


    public void stopEvent() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
            log.info("이벤트 진행 시간 끝, 이벤트 스케줄러 중단");
        }
    }

}