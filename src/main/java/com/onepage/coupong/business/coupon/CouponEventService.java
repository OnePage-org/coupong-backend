package com.onepage.coupong.business.coupon;

import com.onepage.coupong.business.coupon.dto.CouponEventListDto;
import com.onepage.coupong.business.coupon.dto.UserRequestDto;
import com.onepage.coupong.implementation.coupon.EventInitialManager;
import com.onepage.coupong.implementation.coupon.IssuanceQueueManager;
import com.onepage.coupong.implementation.coupon.enums.EventExceptionType;
import com.onepage.coupong.jpa.coupon.CouponEvent;
import com.onepage.coupong.implementation.coupon.EventException;
import com.onepage.coupong.implementation.coupon.EventProgressManager;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import com.onepage.coupong.global.scheduler.CouponEventScheduler;
import com.onepage.coupong.implementation.leaderboard.LeaderboardQueueManager;
import com.onepage.coupong.presentation.coupon.CouponEventUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponEventService implements CouponEventUseCase {

    private final LeaderboardQueueManager leaderboardQueueManager;
    private final IssuanceQueueManager issuanceQueueManager;
    private final CouponEventScheduler couponEventScheduler;
    private final EventInitialManager eventInitialManager;

    private Map<CouponCategory, EventProgressManager> eventProgressManagers;
    private Map<CouponCategory, CouponEvent> couponEvents;

    @PostConstruct
    public void initializeMaps() {
        this.eventProgressManagers = new HashMap<>();
        this.couponEvents = new HashMap<>();
    }

    //@Scheduled(cron = "00 50 23 * * ?")
    @Scheduled(fixedRate = 5000) //테스트 용
    public void scheduleDailyEvents() {
        getTomorrowEvents().forEach(this::registerEvent);
    }

    private List<CouponEvent> getTomorrowEvents() {
        return eventInitialManager.getTomorrowEvents();
    }

    private void registerEvent(CouponEvent event) {
        couponEvents.put(event.getCategory(), event);
        couponEventScheduler.scheduleEvent(event, this);
        initializeEventManager(event);
    }

    public void initializeEventManager(CouponEvent couponEvent) {
        checkConflictEvent(couponEvent);
        eventProgressManagers.put(couponEvent.getCategory(), EventProgressManager.of(couponEvent));
    }

    private void checkConflictEvent(CouponEvent couponEvent) {
        if (eventProgressManagers.containsKey(couponEvent.getCategory())) {
            throw new EventException(EventExceptionType.EVENT_ALREADY_INITIALIZED);
        }
    }

    public boolean addUserToQueue(UserRequestDto userRequestDto) {
        CouponCategory category = userRequestDto.getCouponCategory();
        validateEvent(category, userRequestDto.getUsername());

        return issuanceQueueManager.addToZSet(
                String.valueOf(category),
                String.valueOf(userRequestDto.getUsername()),
                userRequestDto.getAttemptAt());
    }

    private void validateEvent(CouponCategory category, String username) {
        if (!isEventInitialized(category)) {
            throw new EventException(EventExceptionType.EVENT_NOT_INITIALIZED);
        }
        if (!isEventStarted(category)) {
            throw new EventException(EventExceptionType.EVENT_NOT_START);
        }
        if (isEventEnded(category)) {
            throw new EventException(EventExceptionType.EVENT_ENDED);
        }
        if (isAlreadyJoined(category, username)) {
            throw new EventException(EventExceptionType.EVENT_ALREADY_JOIN);
        }
    }

    public void publishCoupons(CouponCategory category, int scheduleCount) {
        EventProgressManager eventProgressManager = eventProgressManagers.get(category);
        CouponEvent couponEvent = couponEvents.get(category);

        validateEventManager(eventProgressManager, category);

        Set<ZSetOperations.TypedTuple<Object>> queueWithScores = issuanceQueueManager.getTopRankSetWithScore(String.valueOf(eventProgressManager.getCouponCategory()), scheduleCount);

        for (ZSetOperations.TypedTuple<Object> item : queueWithScores) {
            if (eventProgressManager.eventEnd()) {
                return;
            }
            processCouponIssuance(eventProgressManager, couponEvent, item);
        }
    }

    private void validateEventManager(EventProgressManager eventProgressManager, CouponCategory category) {
        if (eventProgressManager == null) {
            throw new EventException(EventExceptionType.EVENT_MANAGER_NOT_INITIALIZED);
        }
    }

    private void processCouponIssuance(EventProgressManager eventProgressManager, CouponEvent couponEvent, ZSetOperations.TypedTuple<Object> item) {
        log.info("발급 정보: {} {} {}", eventProgressManager.getCouponCategory(), item.getValue(), item.getScore());
        leaderboardQueueManager.addToZSet(String.valueOf(eventProgressManager.getCouponCategory()), String.valueOf(item.getValue()), item.getScore());
        issuanceQueueManager.removeItemFromZSet(String.valueOf(eventProgressManager.getCouponCategory()), String.valueOf(item.getValue()));
        eventProgressManager.decreaseCouponCount();

        int eventManagerListIndex = eventProgressManager.getPublish_nums() - (eventProgressManager.getCouponCount() + 1);
        eventProgressManager.addUserCoupon(String.valueOf(item.getValue()), couponEvent.getCouponList().get(eventManagerListIndex));
    }

    public boolean isEventStarted(CouponCategory category) {
        CouponEvent couponEvent = couponEvents.get(category);
        if (couponEvent == null) {
            throw new EventException(EventExceptionType.EVENT_NOT_FOUND);
        }
        return LocalDateTime.now().isAfter(couponEvent.getDate());
    }

    private boolean isEventEnded(CouponCategory category) {
        return couponEventScheduler.isSchedulerStopped(category);
    }

    private boolean isAlreadyJoined(CouponCategory category, String userName) {
        return issuanceQueueManager.isUserInQueue(String.valueOf(category), userName) || leaderboardQueueManager.isUserInQueue(String.valueOf(category), userName);
    }

    public Set<Object> getLeaderBoardQueue(String queueCategory) {
        return leaderboardQueueManager.getZSet(queueCategory);
    }

    public Set<Object> getIssuanceQueue(String queueCategory) {
        return issuanceQueueManager.getZSet(queueCategory);
    }

    public boolean isEventInitialized(CouponCategory category) {
        return eventProgressManagers.containsKey(category);
    }

    public boolean validEnd(CouponCategory category) {
        EventProgressManager eventProgressManager = eventProgressManagers.get(category);
        return eventProgressManager != null && eventProgressManager.eventEnd();
    }

    public Map<CouponCategory, EventProgressManager> getAllInitializedEvents() {
        return new HashMap<>(eventProgressManagers);
    }

    public void startEvent(CouponEvent event) {
        log.info("이벤트 시작: {}", event.getId());
    }

    @Override
    public List<CouponEventListDto> getCouponEventList() {
        Map<CouponCategory, EventProgressManager> activeEvents = this.getAllInitializedEvents();
        if (activeEvents.isEmpty()) {
            throw new EventException(EventExceptionType.EVENT_NOT_START);
        }

        List<CouponEventListDto> eventListDto = new ArrayList<>();
        for (EventProgressManager eventProgressManager : activeEvents.values()) {
            eventListDto.add(
                    CouponEventListDto.builder()
                            .eventName(eventProgressManager.getCouponName())
                            .eventCategory(String.valueOf(eventProgressManager.getCouponCategory()))
                            .startTime(eventProgressManager.getStartTime())
                            .build());
        }
        return eventListDto;
    }
}