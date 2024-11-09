package com.onepage.coupong.presentation.coupon;

import com.onepage.coupong.business.coupon.dto.CouponEventListDto;
import com.onepage.coupong.jpa.coupon.CouponEvent;
import com.onepage.coupong.jpa.coupon.EventManager;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import com.onepage.coupong.business.coupon.dto.UserRequestDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CouponEventUseCase {
    void scheduleDailyEvents();

    void initializeEvent(CouponEvent couponEvent);

    boolean isEventInitialized(CouponCategory category);

    boolean addUserToQueue(UserRequestDto userRequestDto);

    void publishCoupons(CouponCategory category, int scheduleCount);

    boolean isEventStarted(CouponCategory category);

    Set<Object> getLeaderBoardQueue(String queueCategory);

    Set<Object> getIssuanceQueue(String queueCategory);

    boolean validEnd(CouponCategory category);

    Map<CouponCategory, EventManager> getAllInitializedEvents();

    void startEvent(CouponEvent event);

    List<CouponEventListDto> getCouponEventList();
}
