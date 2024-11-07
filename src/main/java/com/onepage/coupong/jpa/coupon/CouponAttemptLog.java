package com.onepage.coupong.jpa.coupon;

import com.onepage.coupong.jpa.user.User;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponAttemptLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_attempt_log_id")
    private Long id;

    private LocalDateTime attemptAt;

    @Column(name = "coupon_category")
    private CouponCategory category;

    private boolean isSuccess;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
