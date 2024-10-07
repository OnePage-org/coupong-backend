package com.onepage.coupong.coupon.service;

import com.onepage.coupong.coupon.dto.UserRequestDto;
import com.onepage.coupong.coupon.domain.Coupon;
import com.onepage.coupong.coupon.domain.CouponEvent;
import com.onepage.coupong.coupon.exception.EventException;
import com.onepage.coupong.leaderboard.domain.CouponWinningLog;
import com.onepage.coupong.coupon.domain.EventManager;
import com.onepage.coupong.coupon.domain.enums.CouponCategory;
import com.onepage.coupong.coupon.repository.CouponEventRepository;
import com.onepage.coupong.global.scheduler.CouponEventScheduler;
import com.onepage.coupong.leaderboard.service.LeaderBoardQueueService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@RequiredArgsConstructor
public class CouponEventService {
    private final IssuanceQueueService issuanceQueueService;
    private final LeaderBoardQueueService leaderBoardQueueService;
    private final CouponEventRepository couponEventRepository;
    // 카테고리별로 이벤트 매니저 관리
    private Map<CouponCategory, EventManager> eventManagers = new ConcurrentHashMap<>();
    private Map<CouponCategory, CouponEvent> couponEvents = new ConcurrentHashMap<>();   //추후 단일 쿠폰 이벤트가 아닌 다중 쿠폰 이벤트가 가능하도록 리펙터링 필요
    private final CouponEventScheduler couponEventScheduler;
    //private int couponListIndex = 0;  // 이거 떄문에 에러 발생. 다중 쿠폰 이벤트 상황에서는 이게 문제

    /*
    1.	Event Lifecycle 관리: 이벤트가 시작되고 종료되는 라이프사이클을 명확히 관리하는 것이 중요합니다. 이벤트가 실행되기 전에 eventManager를 미리 준비해두고, 이벤트 상태를 유지하는 방법이 필요합니다.
	2.	의존성 주입: eventManager와 같은 상태를 갖는 객체는 보통 서비스 레벨에서 명확하게 관리되거나, 싱글톤 패턴 또는 상태 관리 객체를 통해 이벤트 상태를 전역적으로 관리하는 방식이 현업에서는 더 선호됩니다.
	3.	컨트롤러에서 이벤트 초기화 로직을 최소화: 컨트롤러는 비즈니스 로직보다는 요청을 처리하는 역할만 수행해야 하므로, 이벤트 초기화 같은 중요한 비즈니스 로직은 서비스 계층에서 다루는 것이 좋습니다.
     */

    // 매일 자정에 호출되어 이벤트 목록을 조회하고 스케줄러에 등록
    //@Scheduled(cron = "00 08 13 * * ?")  // 매일 오후 11시 50분에 실행
    @Scheduled(fixedDelay = 10000000) //테스트용
    public void scheduleDailyEvents() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        LocalDateTime startOfDay = tomorrow.atStartOfDay();  // 내일 00:00:00
        LocalDateTime endOfDay = tomorrow.atTime(23, 59, 59);

        // 내일 진행될 이벤트 조회
        //List<CouponEvent> events = couponEventRepository.findAllByDateBetween(startOfDay, endOfDay);

        //테스트용
        List<CouponEvent> events = couponEventRepository.findAllByDateBetween(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59));


        log.info("!!!!!!!!! \n\n\n\n   "+"!!!!!!");

        log.info("!!!!!!!!! "+LocalDate.now().plusDays(1).atStartOfDay()+ "  " + events);

        for (CouponEvent event : events) {
            couponEvents.put(event.getCategory(), event);

            log.info("이벤트 초기화: 카테고리 = {}, 쿠폰 수 = {}, 시작 시간 = {}",
                    event.getCategory(), event.getCoupon_publish_nums(), event.getDate() + "\n");

            // 각 이벤트마다 스케줄을 동적으로 등록
            couponEventScheduler.scheduleEvent(event, this);
            initializeEvent(event);  //전날 미리 초기화하자
        }
    }

    // 이벤트 초기화
    public void initializeEvent(CouponEvent couponEvent) {
        if (eventManagers.containsKey(couponEvent.getCategory())) {
            throw new IllegalStateException("이미 초기화된 이벤트가 있습니다: 카테고리 = " + couponEvent.getCategory());
        }

        EventManager eventManager = new EventManager(couponEvent.getName(), couponEvent.getCategory(), couponEvent.getDate(), couponEvent.getCoupon_publish_nums(), 0);
        eventManagers.put(couponEvent.getCategory(), eventManager);

        log.info("이벤트 초기화 완료: 카테고리 = {}", couponEvent.getCategory());
    }

    public boolean isEventInitialized(CouponCategory category) {
        return eventManagers.containsKey(category);
    }

    public boolean addUserToQueue (UserRequestDto userRequestDto) {

        log.info("1  : " + userRequestDto);

        CouponCategory category = userRequestDto.getCouponCategory();

        // 이벤트 초기화 확인
        if (!isEventInitialized(category)) {
            log.info("2  : " + category);
            throw new IllegalStateException("이벤트가 초기화되지 않았습니다: 카테고리 = " + category);
        }

        // 이벤트 시작 여부 확인
        if (!isEventStarted(category)) {
            log.info("2-2  : " + category);
            throw new EventException("이벤트가 아직 시작되지 않았습니다: 카테고리 = " + category);
        }

        /*// 이미 발급받은 유저는 리더보드에서 확인 후 중복 발급 방지
        if (leaderBoardQueueService.isMember(String.valueOf(category), String.valueOf(userRequestDto.getId()))) {
            log.warn("유저가 이미 쿠폰을 발급받았습니다. ID: {}", userRequestDto.getId());
            return false;
        }*/

        return issuanceQueueService.addToZSet(
                String.valueOf(userRequestDto.getCouponCategory()),
                String.valueOf(userRequestDto.getId()),
                userRequestDto.getAttemptAt());
    }

    public void publishCoupons(CouponCategory category, int scheduleCount) {
        EventManager eventManager = eventManagers.get(category);
        CouponEvent couponEvent = couponEvents.get(category);  // 발급하고 있는 해당 쿠폰이벤트 리스트에서 쿠폰 id를 받아와야함

        log.info( " \n 남은 쿠폰 개수 :" +  String.valueOf(eventManager.getCouponCount()) +" \n");

        if (eventManager == null) {
            throw new IllegalStateException("이벤트 매니저가 초기화되지 않았습니다: 카테고리 = " + category);
        }

        Set<ZSetOperations.TypedTuple<Object>> queueWithScores = issuanceQueueService.getTopRankSetWithScore(String.valueOf(eventManager.getCouponCategory()), scheduleCount);

        System.out.println(queueWithScores.size() +" 큐 사이즈 !");

        for (ZSetOperations.TypedTuple<Object> item : queueWithScores) {
            if(eventManager.eventEnd()) {

                System.out.println("\n\n발행이 모두 끝이 났습니다. 데이터 확인\n\n");

                Map<Object, Coupon> temp = eventManager.getUserCouponMap();

                for (Map.Entry<Object, Coupon> entry : temp.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }

                return;
            }
            log.info("발급 정보 : "  + eventManager.getCouponCategory() +" " + item.getValue() +" "+ item.getScore());

            leaderBoardQueueService.addToZSet(String.valueOf(eventManager.getCouponCategory()), String.valueOf(item.getValue()), item.getScore());

            issuanceQueueService.removeItemFromZSet(String.valueOf(eventManager.getCouponCategory()), String.valueOf(item.getValue()));
            eventManager.decreaseCouponCount();

            int eventManagerListIndex =  eventManager.getPublish_nums() - (eventManager.getCouponCount() + 1);  // 계산 잘 해야됨.

            log.info("쿠폰 발급 성공자 리스트 생성 중" + eventManagerListIndex+" 번째 인덱스에 " +couponEvent.getCategory()+" 이벤트 리스트");

            eventManager.addUserCoupon(String.valueOf(item.getValue()), couponEvent.getCouponList().get(eventManagerListIndex));  // couponListIndex 이게 문제를 일으킴 다중 이벤트 환경에서
        }
    }

    // 해당 카테고리의 이벤트가 시작했는지 확인하는 메서드
    public boolean isEventStarted(CouponCategory category) {
        CouponEvent couponEvent = couponEvents.get(category);
        if (couponEvent == null) {
            throw new IllegalStateException("쿠폰 이벤트가 존재하지 않습니다: 카테고리 = " + category);
        }

        // 현재 시간이 이벤트 시작 시간 이후인지 확인
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(couponEvent.getDate());
    }


    public Set<Object> getLeaderBoardQueue(String queueCategory) {
        return leaderBoardQueueService.getZSet(queueCategory);
    }

    public Set<Object> getIssuanceQueue(String queueCategory) {
        return issuanceQueueService.getZSet(queueCategory);
    }

    //쿠폰 발행된 사람들 데이터 RDB 영속
    private void persistEventAttemptData (CouponWinningLog couponWinningLog) {

    }

    public boolean validEnd(CouponCategory category) {
        EventManager eventManager = eventManagers.get(category);
        return eventManager != null && eventManager.eventEnd();
    }

    // 초기화된 모든 이벤트 목록 반환 (카테고리별로)
    public Map<CouponCategory, EventManager> getAllInitializedEvents() {
        return new HashMap<>(eventManagers);  // 카테고리별 초기화된 모든 이벤트 매니저 반환
    }

    public void startEvent(CouponEvent event) {
        log.info("이벤트 시작: {}", event.getId());
    }
}