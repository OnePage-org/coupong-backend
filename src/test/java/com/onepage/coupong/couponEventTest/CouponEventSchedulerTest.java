package com.onepage.coupong.couponEventTest;


import com.onepage.coupong.entity.CouponEvent;
//import com.onepage.coupong.scheduler.CouponEventScheduler;
import com.onepage.coupong.entity.enums.CouponCategory;
import com.onepage.coupong.repository.CouponEventRepository;
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
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//public class CouponEventSchedulerTest {
//
//    @Autowired
//    private CouponEventScheduler couponEventScheduler;
//
//    @MockBean
//    private CouponEventService couponEventService;
//
//    @MockBean
//    private CouponEventRepository couponEventRepository;
//
//    @MockBean
//    private ThreadPoolTaskScheduler taskScheduler;
//
//    @Test
//    public void testScheduleEvent() {
//        // Given: 이벤트 데이터 설정
//        CouponEvent event = CouponEvent.builder()
//                .category(CouponCategory.DEFAULT)
//                .date(LocalDateTime.now().plusMinutes(1))
//                .coupon_publish_nums(30) // 발행할 쿠폰 개수
//                .duration("30")  // 이벤트 지속 시간
//                .build();
//
//
//        when(couponEventService.isEventInitialized()).thenReturn(true);
//
//        ScheduledFuture<?> future = mock(ScheduledFuture.class);
//        when(taskScheduler.scheduleWithFixedDelay(any(Runnable.class), anyLong())).thenReturn(future);
//
//        // When: 이벤트 스케줄링
//        couponEventScheduler.scheduleEvent(event, couponEventService);
//
//        // Then: 스케줄러가 설정되었는지 확인
//        verify(taskScheduler, times(2)).schedule(any(Runnable.class), any(Date.class));
//        verify(taskScheduler).scheduleWithFixedDelay(any(Runnable.class), eq(10000L));
//    }
//}

