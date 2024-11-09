package com.onepage.coupong.presentation.coupon;

import com.onepage.coupong.business.coupon.dto.EventAttemptDto;
import com.onepage.coupong.business.coupon.dto.CouponEventDto;
import com.onepage.coupong.jpa.coupon.CouponEvent;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;

import java.util.List;

public interface CouponEventUseCase {
    // 특정 이벤트를 초기화
    void initializeEventManager(CouponEvent event);

    // 특정 이벤트가 초기화되었는지 확인
    boolean isEventInitialized(CouponCategory category);

    // 사용자를 이벤트 큐에 추가
    void addUserToQueue(EventAttemptDto eventAttemptDto);

    // 쿠폰 발급 수행
    void publishCoupons(CouponCategory category, int scheduleCount);

    // 이벤트 시작 여부 확인
    boolean isEventStarted(CouponCategory category);

    // 모든 이벤트 목록을 DTO 형태로 반환
    List<CouponEventDto> getCouponEventList();
}
