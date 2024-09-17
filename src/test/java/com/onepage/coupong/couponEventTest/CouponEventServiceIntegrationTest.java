package com.onepage.coupong.couponEventTest;

import com.onepage.coupong.dto.UserRequestDto;
import com.onepage.coupong.entity.enums.CouponCategory;
import com.onepage.coupong.service.CouponEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class CouponEventServiceIntegrationTest {
    long userId = 1;

    @Autowired
    CouponEventService couponEventService;

    @Test
    void 선착순_100명에게_30개_쿠폰_발행() throws InterruptedException {
        final CouponCategory couponCategory = CouponCategory.CHICKEN;
        final int attempt = 100;
        final int couponCount = 30;
        final int endNums = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(attempt);
        couponEventService.setEventManager(couponCategory, couponCount, endNums);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker(countDownLatch, couponCategory)))
                .limit(attempt)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(5000);

        final long failEventPeopleNums = couponEventService.getQueue().size();
        assertEquals(attempt - couponCount, failEventPeopleNums);

    }

    private class AddQueueWorker implements Runnable {
        private CountDownLatch countDownLatch;

        public AddQueueWorker(CountDownLatch countDownLatch, CouponCategory couponCategory) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                UserRequestDto userRequestDto = UserRequestDto.builder()
                        .id(userId++)
                        .username("test")
                        .email("test@test.com")
                        .couponCategory(CouponCategory.CHICKEN)
                        .build();
                couponEventService.addUserToQueue(userRequestDto);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

}
