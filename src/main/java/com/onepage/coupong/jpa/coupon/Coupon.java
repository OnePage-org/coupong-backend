package com.onepage.coupong.jpa.coupon;

import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_name")
    private String name;

    @Column(name = "coupon_number")
    private UUID number;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_category")
    private CouponCategory category;

    private LocalDateTime expirationDate;

    @Column(name = "coupon_event_id")
    private Long couponEventId;
}
