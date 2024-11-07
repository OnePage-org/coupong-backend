package com.onepage.coupong.leaderboard.domain;

import com.onepage.coupong.jpa.coupon.Coupon;
import com.onepage.coupong.jpa.user.User;
import com.onepage.coupong.leaderboard.domain.enums.WinningCouponState;
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
public class CouponWinningLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_winning_log_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "winning_coupon_state")
    private WinningCouponState winningCouponState;

    private LocalDateTime winningDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private User winner;

}
