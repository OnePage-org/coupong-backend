package com.onepage.coupong.repository;

import com.onepage.coupong.entity.CouponAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponAttemptRepository extends JpaRepository<CouponAttempt,Long> {
}
