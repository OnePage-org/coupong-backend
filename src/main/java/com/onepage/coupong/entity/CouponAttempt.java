package com.onepage.coupong.entity;

import com.onepage.coupong.entity.enums.CouponCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_attempt_id")
    private Long id;

    private String userId;

    private LocalDateTime attemptAt;

    private CouponCategory couponCategory;

    private boolean isSuccess;
}
