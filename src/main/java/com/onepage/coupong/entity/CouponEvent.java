package com.onepage.coupong.entity;

import com.onepage.coupong.entity.enums.CouponEventState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_event_id")
    private Long id;

    @Column(name = "coupon_event_date")
    private LocalDateTime eventDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_event_state")
    private CouponEventState eventState;

    private LocalDateTime winningDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private User winner;

}
