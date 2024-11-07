package com.onepage.coupong.persistence.leaderboard;

import com.onepage.coupong.jpa.leaderboard.CouponWinningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponWinningRepository extends JpaRepository<CouponWinningLog,Long> {
}
