package com.onepage.coupong.controller;

import com.onepage.coupong.dto.UserRequestDto;
import com.onepage.coupong.service.CouponEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupon")
public class CouponEventController {

    private final CouponEventService couponEventService;

    public CouponEventController(CouponEventService couponEventService) {
        this.couponEventService = couponEventService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestCoupon(@RequestBody UserRequestDto userRequestDto) {
        boolean publishSuccess = couponEventService.addUserToQueue(userRequestDto);

        if (publishSuccess) {
            return ResponseEntity.ok("쿠폰 발급 요청에 성공했습니다 - 대기열 등록 -");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("쿠폰 발급 요청 실패");
        }
    }
}
