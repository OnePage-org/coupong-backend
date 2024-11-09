package com.onepage.coupong.implementation.coupon;

import com.onepage.coupong.jpa.coupon.CouponEvent;
import com.onepage.coupong.persistence.coupon.CouponEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventInitialManager {

    private final CouponEventRepository couponEventRepository;

    public List<CouponEvent> getTomorrowEvents() {
        LocalDateTime startOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.withHour(23).withMinute(59).withSecond(59);
        return couponEventRepository.findAllByDateBetween(startOfDay, endOfDay);
    }
}
