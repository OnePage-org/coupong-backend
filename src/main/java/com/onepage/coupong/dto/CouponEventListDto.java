package com.onepage.coupong.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
public class CouponEventListDto {
    private String eventName;
    private String eventCategory;
    private LocalDateTime startTime;
}
