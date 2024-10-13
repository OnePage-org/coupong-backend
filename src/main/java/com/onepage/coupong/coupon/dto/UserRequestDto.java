package com.onepage.coupong.coupon.dto;

import com.onepage.coupong.coupon.domain.enums.CouponCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequestDto {
    private Long id;
    private CouponCategory couponCategory;
    private Long attemptAt;
    private String username;
}
