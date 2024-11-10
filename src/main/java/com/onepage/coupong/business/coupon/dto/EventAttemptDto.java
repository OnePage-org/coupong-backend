package com.onepage.coupong.business.coupon.dto;

import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventAttemptDto {
    private Long id;
    private CouponCategory couponCategory;
    private Long attemptAt;
    private String username;
}
