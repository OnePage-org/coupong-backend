package com.onepage.coupong.service;

import com.onepage.coupong.dto.UserRequestDto;
import com.onepage.coupong.entity.CouponEvent;
import com.onepage.coupong.entity.CouponWinningLog;
import com.onepage.coupong.entity.EventManager;
import com.onepage.coupong.entity.enums.CouponCategory;
import com.onepage.coupong.repository.CouponEventRepository;
import com.onepage.coupong.scheduler.CouponEventScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;



@Slf4j
@Service
@RequiredArgsConstructor
public class CouponEventService {
    private final IssuanceQueueService issuanceQueueService;
    private final LeaderBoardQueueService leaderBoardQueueService;
    private final CouponEventRepository couponEventRepository;
    private EventManager eventManager;
    private final CouponEventScheduler couponEventScheduler;

    /*
    1.	Event Lifecycle 관리: 이벤트가 시작되고 종료되는 라이프사이클을 명확히 관리하는 것이 중요합니다. 이벤트가 실행되기 전에 eventManager를 미리 준비해두고, 이벤트 상태를 유지하는 방법이 필요합니다.
	2.	의존성 주입: eventManager와 같은 상태를 갖는 객체는 보통 서비스 레벨에서 명확하게 관리되거나, 싱글톤 패턴 또는 상태 관리 객체를 통해 이벤트 상태를 전역적으로 관리하는 방식이 현업에서는 더 선호됩니다.
	3.	컨트롤러에서 이벤트 초기화 로직을 최소화: 컨트롤러는 비즈니스 로직보다는 요청을 처리하는 역할만 수행해야 하므로, 이벤트 초기화 같은 중요한 비즈니스 로직은 서비스 계층에서 다루는 것이 좋습니다.
     */

// 매일 자정에 호출되어 이벤트 목록을 조회하고 스케줄러에 등록
@Scheduled(cron = "00 20 14 * * ?")  // 매일 오후 11시 50분에 실행
public void scheduleDailyEvents() {

    LocalDate tomorrow = LocalDate.now().plusDays(1);

    LocalDateTime startOfDay = tomorrow.atStartOfDay();  // 내일 00:00:00
    LocalDateTime endOfDay = tomorrow.atTime(23, 59, 59);

    // 내일 진행될 이벤트 조회
    List<CouponEvent> events = couponEventRepository.findAllByDateBetween(startOfDay, endOfDay);



    log.info("!!!!!!!!! \n\n\n\n   "+"!!!!!!");

    log.info("!!!!!!!!! "+LocalDate.now().plusDays(1).atStartOfDay()+ "  " + events);

    for (CouponEvent event : events) {
        log.info("이벤트 초기화: 카테고리 = {}, 쿠폰 수 = {}, 시작 시간 = {}",
                event.getCategory(), event.getCoupon_publish_nums(), event.getDate());

        // 각 이벤트마다 스케줄을 동적으로 등록
        couponEventScheduler.scheduleEvent(event, this);
    }
}

// 이벤트 초기화 메서드
public void initializeEvent(CouponCategory couponCategory, int couponCount, int endNums) {
    if (this.eventManager != null) {
        throw new IllegalStateException("이미 초기화된 이벤트가 있습니다.");
    }
    this.eventManager = new EventManager(couponCategory, couponCount, endNums);
    log.info("이벤트 초기화: 카테고리 = {}, 쿠폰 수 = {}, 종료 조건 = {}", couponCategory, couponCount, endNums);
}

// 이벤트 초기화 여부 확인
public boolean isEventInitialized() {
    return this.eventManager != null;
}

public boolean addUserToQueue (UserRequestDto userRequestDto) {
    return issuanceQueueService.addToZSet(
            String.valueOf(userRequestDto.getCouponCategory()),
            String.valueOf(userRequestDto.getId()),
            userRequestDto.getAttemptAt());
}

public void publishCoupons(int scheduleCount) {
    Set<ZSetOperations.TypedTuple<Object>> queueWithScores = issuanceQueueService.getTopRankSetWithScore(String.valueOf(eventManager.getCouponCategory()), scheduleCount);

    System.out.println(queueWithScores.size() +" 큐 사이즈 !");

    for (ZSetOperations.TypedTuple<Object> item : queueWithScores) {
        if(eventManager.eventEnd()) return;

        System.out.println( eventManager.getCouponCategory() +" " + item.getValue() +" "+ item.getScore());

        /*
        for (Object userId : queue) {

            System.out.println("반목문 돌립니다 큐에서 유저 id 꺼내옴 " + userId);


            CouponWinningLog couponWinningLog = CouponWinningLog.builder()
                    .winningCouponState(WinningCouponState.COMPLETED)
                    .winningDate(LocalDateTime.now())
                    .build();

            couponWinningRepository.save(couponWinningLog);  쿠폰 발행 성공자 Set 자료구조 mySQl RDB -> Redis ZSet
             */

        // 쿠폰 발행 성공자 정보를 Redis ZSet에 추가

        // 이때, 기존 발행 성공자 대기열에 해당 유저가 있을 경우 빡구 시킬지 말지 ? 한 카테고리 내에서는 한번만 받을 수 있게 하자 ! 이거 추가 필요

        leaderBoardQueueService.addToZSet(String.valueOf(eventManager.getCouponCategory()), String.valueOf(item.getValue()), item.getScore());

        issuanceQueueService.removeItemFromZSet(String.valueOf(eventManager.getCouponCategory()), String.valueOf(item.getValue()));
        eventManager.decreaseCouponCount();
    }
}

public Set<Object> getQueue(String queueCategory) {
    return issuanceQueueService.getZSet(queueCategory);
}

//쿠폰 발행된 사람들 데이터 RDB 영속
private void persistEventAttemptData (CouponWinningLog couponWinningLog) {

}

public boolean validEnd() {
    return this.eventManager != null && this.eventManager.eventEnd();
}
}
