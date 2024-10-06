package com.onepage.coupong.couponEventTest;

import com.onepage.coupong.coupon.dto.UserRequestDto;
import com.onepage.coupong.coupon.domain.enums.CouponCategory;
import com.onepage.coupong.coupon.service.CouponEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

/*    @Test
    void 선착순_100명에게_30개_쿠폰_발행() throws InterruptedException {
        final CouponCategory couponCategory = CouponCategory.PIZZA;
        final int attempt = 100;
        final int couponCount = 37;
        final int endNums = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(attempt);
        //couponEventService.initializeEvent( couponCategory, couponCount, endNums);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker(countDownLatch, couponCategory)))
                .limit(attempt)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(5000);

        final long failEventPeopleNums = couponEventService.getIssuanceQueue(String.valueOf(couponCategory)).size();
        assertEquals(attempt - couponCount, failEventPeopleNums);

    }*/

    @Test
    void 선착순_100명에게_쿠폰_발행_스케줄_동적_할당_피자() throws InterruptedException {
        final CouponCategory couponCategory = CouponCategory.PIZZA;
        final int attempt = 100;

        final CountDownLatch countDownLatch = new CountDownLatch(attempt);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker(countDownLatch, couponCategory)))
                .limit(attempt)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(30000);

        final long failEventPeopleNums = couponEventService.getIssuanceQueue(String.valueOf(couponCategory)).size();
        final long successEventPeopleNums = couponEventService.getLeaderBoardQueue(String.valueOf(couponCategory)).size();

        assertEquals(attempt - successEventPeopleNums, failEventPeopleNums);

    }

    private class AddQueueWorker implements Runnable {
        private CountDownLatch countDownLatch;

        public AddQueueWorker(CountDownLatch countDownLatch, CouponCategory couponCategory) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {

                LocalDateTime localDateTime
                        = LocalDateTime.now();

                String localDateTimeFormat1
                        = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

                UserRequestDto userRequestDto = UserRequestDto.builder()
                        .id(userId++)
                        .couponCategory(CouponCategory.PIZZA)
                        .attemptAt(Long.valueOf(localDateTimeFormat1))
                        .build();


                couponEventService.addUserToQueue(userRequestDto);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    @Test
    void 선착순_100명에게_쿠폰_발행_스케줄_동적_할당_커피() throws InterruptedException {
        final CouponCategory couponCategory = CouponCategory.COFFEE;
        final int attempt = 100;

        final CountDownLatch countDownLatch = new CountDownLatch(attempt);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker2(countDownLatch, couponCategory)))
                .limit(attempt)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(30000);

        final long failEventPeopleNums = couponEventService.getIssuanceQueue(String.valueOf(couponCategory)).size();
        final long successEventPeopleNums = couponEventService.getLeaderBoardQueue(String.valueOf(couponCategory)).size();

        assertEquals(attempt - successEventPeopleNums, failEventPeopleNums);

    }

    private class AddQueueWorker2 implements Runnable {
        private CountDownLatch countDownLatch;

        public AddQueueWorker2(CountDownLatch countDownLatch, CouponCategory couponCategory) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {

                LocalDateTime localDateTime
                        = LocalDateTime.now();

                String localDateTimeFormat1
                        = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

                UserRequestDto userRequestDto = UserRequestDto.builder()
                        .id(userId++)
                        .couponCategory(CouponCategory.COFFEE)
                        .attemptAt(Long.valueOf(localDateTimeFormat1))
                        .build();


                couponEventService.addUserToQueue(userRequestDto);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

}
