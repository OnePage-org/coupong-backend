package com.onepage.coupong.coupon.repository;

import com.onepage.coupong.coupon.domain.CouponWinningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponWinningRepository extends JpaRepository<CouponWinningLog,Long> {
}
