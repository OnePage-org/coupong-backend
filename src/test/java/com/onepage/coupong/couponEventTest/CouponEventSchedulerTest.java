package com.onepage.coupong.couponEventTest;


import com.onepage.coupong.entity.CouponEvent;
//import com.onepage.coupong.scheduler.CouponEventScheduler;
import com.onepage.coupong.scheduler.CouponEventScheduler;
import com.onepage.coupong.service.CouponEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CouponEventSchedulerTest {

    @MockBean
    private CouponEventService couponEventService;

    @Autowired
    private CouponEventScheduler couponEventScheduler;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Test
    public void testScheduleEvent_StartsAndStopsCorrectly() throws InterruptedException {
        // given
        CouponEvent event = new CouponEvent();
        event.setDate(LocalDateTime.now().plusSeconds(2)); // 2초 후 시작
        event.setDuration("1"); // 1분짜리 이벤트

        // Mock: isEventInitialized() -> true
        when(couponEventService.isEventInitialized()).thenReturn(true);

        // when: 스케줄러 시작
        couponEventScheduler.scheduleEvent(event, couponEventService);

        // 3초 후에 쿠폰 발행이 발생했는지 확인
        Thread.sleep(3000);
        verify(couponEventService, times(1)).publishCoupons(10);

        // when: 스케줄러 종료
        Thread.sleep(2000); // 추가 시간이 지나면서 이벤트 종료가 되었는지 확인
        verify(couponEventService, times(1)).validEnd();
    }

    @Test
    public void testSchedulerStopWhenEventEnds() throws InterruptedException {
        // given
        CouponEvent event = new CouponEvent();
        event.setDate(LocalDateTime.now().plusSeconds(1)); // 1초 후 시작
        event.setDuration("1"); // 1분짜리 이벤트

        // Mock: validEnd() -> true to trigger event stop
        when(couponEventService.validEnd()).thenReturn(true);

        // when: 스케줄러 시작
        couponEventScheduler.scheduleEvent(event, couponEventService);

        // 충분한 대기 시간 설정 (예: 1분)
        Thread.sleep(60000);  // 스케줄러가 실행 및 중지되기를 기다림

        // then: 스케줄러가 중단되었는지 확인
        assertTrue(couponEventScheduler.getScheduledTask().isCancelled());

        // stopEvent()가 호출되었는지 확인
        verify(couponEventService, times(1)).validEnd();
    }


}

