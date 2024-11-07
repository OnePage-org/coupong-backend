package com.onepage.coupong.couponEventTest;

import com.onepage.coupong.business.coupon.dto.UserRequestDto;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import com.onepage.coupong.business.coupon.CouponEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        Thread.sleep(50000);

        final long failEventPeopleNums = couponEventService.getIssuanceQueue(String.valueOf(couponCategory)).size();
        final long successEventPeopleNums = couponEventService.getLeaderBoardQueue(String.valueOf(couponCategory)).size();

        assertEquals(attempt - successEventPeopleNums, failEventPeopleNums);
    }

    @Test
    void 선착순_100명에게_쿠폰_발행_스케줄_동적_할당_커피() throws InterruptedException {
        final CouponCategory couponCategory = CouponCategory.COFFEE;
        final int attempt = 100;

        final CountDownLatch countDownLatch = new CountDownLatch(attempt);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker(countDownLatch, couponCategory)))
                .limit(attempt)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(50000);

        final long failEventPeopleNums = couponEventService.getIssuanceQueue(String.valueOf(couponCategory)).size();
        final long successEventPeopleNums = couponEventService.getLeaderBoardQueue(String.valueOf(couponCategory)).size();

        assertEquals(attempt - successEventPeopleNums, failEventPeopleNums);
    }

    @Test
    void 선착순_100명에게_쿠폰_발행_스케줄_동적_할당_커피_앤_피자() throws InterruptedException {
        final CouponCategory couponCategoryCoffee = CouponCategory.COFFEE;
        final CouponCategory couponCategoryPizza = CouponCategory.PIZZA;

        final int attempt = 100;

        final CountDownLatch countDownLatch = new CountDownLatch(attempt);

        /*List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorkerCoffee(countDownLatch, couponCategory)))
                .limit(attempt)
                .collect(Collectors.toList());*/

        List<Thread> workers = new ArrayList<>();

        boolean swap = true;

        for (int i = 0; i < attempt; i++) {
            if(swap) {
                workers.add(new Thread(new AddQueueWorker(countDownLatch, couponCategoryCoffee)));
            } else {
                workers.add(new Thread(new AddQueueWorker(countDownLatch, couponCategoryPizza)));
            }
            swap = !swap;
        }

        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(100000);

        final long failEventPeopleNumsPizza = couponEventService.getIssuanceQueue(String.valueOf(couponCategoryCoffee)).size();
        final long successEventPeopleNumsPizza = couponEventService.getLeaderBoardQueue(String.valueOf(couponCategoryCoffee)).size();

        final long failEventPeopleNumsCoffee = couponEventService.getIssuanceQueue(String.valueOf(couponCategoryPizza)).size();
        final long successEventPeopleNumsCoffee = couponEventService.getLeaderBoardQueue(String.valueOf(couponCategoryPizza)).size();

        assertEquals(attempt / 2 - successEventPeopleNumsPizza, failEventPeopleNumsPizza);
        assertEquals(attempt / 2 - successEventPeopleNumsCoffee, failEventPeopleNumsCoffee);

    }



    private class AddQueueWorker implements Runnable {
        private CountDownLatch countDownLatch;
        private CouponCategory couponCategory;

        public AddQueueWorker(CountDownLatch countDownLatch, CouponCategory couponCategory) {
            this.countDownLatch = countDownLatch;
            this.couponCategory = couponCategory;
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
                        .couponCategory(couponCategory)
                        .attemptAt(Long.valueOf(localDateTimeFormat1))
                        .couponCategory(CouponCategory.DEFAULT)
                        .attemptAt(System.currentTimeMillis())
                        .build();


                couponEventService.addUserToQueue(userRequestDto);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

/*    private class AddQueueWorkerCoffee implements Runnable {
        private CountDownLatch countDownLatch;
        private CouponCategory couponCategory;

        public AddQueueWorkerCoffee(CountDownLatch countDownLatch, CouponCategory couponCategory) {
            this.countDownLatch = countDownLatch;
            this.couponCategory = couponCategory;
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
                        .couponCategory(couponCategory)
                        .attemptAt(Long.valueOf(localDateTimeFormat1))
                        .build();


                couponEventService.addUserToQueue(userRequestDto);
            } finally {
                countDownLatch.countDown();
            }
        }
    }*/

}
