package com.onepage.coupong.leaderboard.repository;

import com.onepage.coupong.leaderboard.domain.CouponWinningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponWinningRepository extends JpaRepository<CouponWinningLog,Long> {
}
