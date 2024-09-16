package com.onepage.coupong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class CouponEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_event_id")
    private Long id;

    @Setter  //테스트용
    @Column(name = "coupon_event_date")
    private LocalDateTime date;

    @Setter //테스트용
    private int coupon_publish_nums;

    @Column(name = "coupon_event_duration")
    private String duration;

    @OneToMany(mappedBy = "couponEvent")
    private List<Coupon> couponList = new ArrayList<>();

}