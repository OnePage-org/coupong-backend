package com.onepage.coupong.jpa.coupon;

import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_event_id")
    private Long id;

    @Column(name = "coupon_event_name")
    private String name;

    @Column(name = "coupon_event_date")
    private LocalDateTime date;

    private int coupon_publish_nums;

    @Column(name = "coupon_category")
    @Enumerated(EnumType.STRING)
    private CouponCategory category;

    @Column(name = "coupon_event_duration")
    private String duration;

    @OneToMany(mappedBy = "couponEventId", fetch = FetchType.LAZY)
    private List<Coupon> couponList;
}