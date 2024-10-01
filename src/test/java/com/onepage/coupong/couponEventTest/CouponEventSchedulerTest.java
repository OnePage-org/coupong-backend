package com.onepage.coupong.couponEventTest;


//import com.onepage.coupong.scheduler.CouponEventScheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

