package com.onepage.coupong.repository;

import com.onepage.coupong.entity.CouponWinningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponWinningRepository extends JpaRepository<CouponWinningLog,Long> {
}
