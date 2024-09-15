package com.onepage.coupong.dto;

import com.onepage.coupong.entity.enums.CouponCategory;
import jdk.jfr.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequestDto {
    private Long id;
    private String username;
    private String email;
    private CouponCategory couponCategory;
    private final long attemptAt = System.currentTimeMillis();
}
