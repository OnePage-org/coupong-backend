package com.onepage.coupong.repository;

import com.onepage.coupong.entity.CouponEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponEventRepository extends JpaRepository<CouponEvent,Long> {
}
