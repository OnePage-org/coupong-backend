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
    private CouponCategory couponCategory;
    private Long attemptAt;
    private String username;
}
