package com.onepage.coupong.implementation.coupon.manager;

import com.onepage.coupong.business.coupon.dto.CouponEventDto;
import com.onepage.coupong.implementation.coupon.EventException;
import com.onepage.coupong.implementation.coupon.enums.EventExceptionType;
import com.onepage.coupong.jpa.coupon.CouponEvent;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import com.onepage.coupong.persistence.coupon.CouponEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventInitialManager {

    private final CouponEventRepository couponEventRepository;

    public List<CouponEvent> getTomorrowEvents() {
        LocalDateTime startOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.withHour(23).withMinute(59).withSecond(59);
        return couponEventRepository.findAllByDateBetween(startOfDay, endOfDay);
    }

    public List<CouponEventDto> getCouponEventList(Map<CouponCategory, EventProgressManager> eventProgressManagers) {
        if (eventProgressManagers.isEmpty()) {
            throw new EventException(EventExceptionType.EVENT_NOT_START);
        }
        List<CouponEventDto> eventListDto = new ArrayList<>();

        for (EventProgressManager eventProgressManager : eventProgressManagers.values()) {
            eventListDto.add(
                    CouponEventDto.builder()
                            .eventName(eventProgressManager.getCouponName())
                            .eventCategory(String.valueOf(eventProgressManager.getCouponCategory()))
                            .startTime(eventProgressManager.getStartTime())
                            .build());
        }
        return eventListDto;
    }
}
