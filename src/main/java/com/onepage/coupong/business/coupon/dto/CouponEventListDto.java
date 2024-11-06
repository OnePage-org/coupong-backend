package com.onepage.coupong.business.coupon.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CouponEventListDto {
    private String eventName;
    private String eventCategory;
    private LocalDateTime startTime;
}
