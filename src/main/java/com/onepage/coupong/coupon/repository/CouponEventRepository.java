package com.onepage.coupong.coupon.repository;

import com.onepage.coupong.coupon.domain.CouponEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponEventRepository extends JpaRepository<CouponEvent, Long> {
    List<CouponEvent> findAllByDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
