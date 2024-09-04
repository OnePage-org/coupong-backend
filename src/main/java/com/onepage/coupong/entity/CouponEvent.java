package com.onepage.coupong.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_event_id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "coupon_event_date")
    private Date eventData;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_event_state")
    private CouponEventState eventState;

    @Temporal(TemporalType.TIMESTAMP)
    private Date winnigDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private User winner;

}
