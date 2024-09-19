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
public class EventScheduler {      //v1 스케줄러

    private final CouponEventService couponEventService;

    // RDB 쿠폰이벤트 테이블에서 이벤트 정보 들고와서 동적으로 스케줄 세팅하기
    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void couponEventScheduler() {
        try {
            if (!couponEventService.isEventInitialized()) {
                log.info("이벤트가 초기화되지 않았습니다.");
                return;
            }
            if (couponEventService.validEnd()) {
                log.info("이벤트 쿠폰 발행 가능 개수 충족");
                return;
            }

            couponEventService.publishCoupons(10);
            log.info("쿠폰 발행 완료");
        } catch (Exception e) {
            log.error("스케줄러에서 오류 발생", e);
        }
    }
}

/*
@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {
    private final CouponEventService couponEventService;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;

    public void scheduleEvent(LocalDateTime startTime, LocalDateTime endTime) {
        long delay = Duration.between(LocalDateTime.now(), startTime).toMillis();
        long period = 10000; // 10초마다 실행

        // 이벤트 시작 시간에 스케줄러 시작
        scheduledFuture = taskScheduler.scheduleAtFixedRate(() -> {
            if (LocalDateTime.now().isAfter(endTime)) {
                log.info("이벤트 시간이 종료되었습니다. 스케줄러를 중지합니다.");
                stopScheduler();
                return;
            }
            if (!couponEventService.validEnd()) {
                couponEventService.publishCoupons(10);
            }
        }, new Date(System.currentTimeMillis() + delay), period);
    }

    public void stopScheduler() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false); // 스케줄러 중지
        }
    }

}
 */