package com.onepage.coupong.business.coupon;

import com.onepage.coupong.business.coupon.dto.EventAttemptDto;
import com.onepage.coupong.business.coupon.dto.CouponEventDto;
import com.onepage.coupong.implementation.coupon.manager.EventInitialManager;
import com.onepage.coupong.implementation.coupon.manager.IssuanceQueueManager;
import com.onepage.coupong.implementation.coupon.enums.EventExceptionType;
import com.onepage.coupong.jpa.coupon.CouponEvent;
import com.onepage.coupong.implementation.coupon.EventException;
import com.onepage.coupong.implementation.coupon.manager.EventProgressManager;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import com.onepage.coupong.global.scheduler.CouponEventScheduler;
import com.onepage.coupong.implementation.leaderboard.manager.LeaderboardQueueManager;
import com.onepage.coupong.presentation.coupon.CouponEventUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponEventService implements CouponEventUseCase {

    private Map<CouponCategory, CouponEvent> couponEvents;
    private final CouponEventScheduler couponEventScheduler;

    private final LeaderboardQueueManager leaderboardQueueManager;
    private final IssuanceQueueManager issuanceQueueManager;

    private final EventInitialManager eventInitialManager;
    private Map<CouponCategory, EventProgressManager> eventProgressManagers;

    @PostConstruct
    public void initializeMaps() {
        this.eventProgressManagers = new HashMap<>();
        this.couponEvents = new HashMap<>();
    }

    @Scheduled(cron = "00 50 23 * * ?")
    public void scheduleDailyEvents() {
        getTomorrowEvents().forEach(this::registerEvent);
    }

    public void addUserToQueue(EventAttemptDto eventAttemptDto) {
        CouponCategory category = eventAttemptDto.getCouponCategory();
        validateEvent(category, eventAttemptDto.getUsername());

        issuanceQueueManager.addToZSet(
                String.valueOf(category),
                String.valueOf(eventAttemptDto.getUsername()),
                System.currentTimeMillis());
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

    private void processCouponIssuance(EventProgressManager eventProgressManager, CouponEvent couponEvent, ZSetOperations.TypedTuple<Object> item) {
        leaderboardQueueManager.addToZSet(String.valueOf(eventProgressManager.getCouponCategory()), String.valueOf(item.getValue()), item.getScore());
        issuanceQueueManager.removeItemFromZSet(String.valueOf(eventProgressManager.getCouponCategory()), String.valueOf(item.getValue()));
        eventProgressManager.decreaseCouponCount();

        int eventManagerListIndex = eventProgressManager.getPublish_nums() - (eventProgressManager.getCouponCount() + 1);
        eventProgressManager.addUserCoupon(String.valueOf(item.getValue()), couponEvent.getCouponList().get(eventManagerListIndex));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponEventDto> getCouponEventList() {
        return eventInitialManager.getCouponEventList(eventProgressManagers);
    }

    @Transactional(readOnly = true)
    protected List<CouponEvent> getTomorrowEvents() {
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

    public boolean isEventInitialized(CouponCategory category) {
        return eventProgressManagers.containsKey(category);
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

    public boolean validEnd(CouponCategory category) {
        EventProgressManager eventProgressManager = eventProgressManagers.get(category);
        return eventProgressManager != null && eventProgressManager.eventEnd();
    }

    private void checkConflictEvent(CouponEvent couponEvent) {
        if (eventProgressManagers.containsKey(couponEvent.getCategory())) {
            throw new EventException(EventExceptionType.EVENT_ALREADY_INITIALIZED);
        }
    }

    private void validateEventManager(EventProgressManager eventProgressManager, CouponCategory category) {
        if (eventProgressManager == null) {
            throw new EventException(EventExceptionType.EVENT_MANAGER_NOT_INITIALIZED);
        }
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
}