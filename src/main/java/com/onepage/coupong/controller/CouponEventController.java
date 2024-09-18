package com.onepage.coupong.controller;

import com.onepage.coupong.dto.UserRequestDto;
import com.onepage.coupong.service.CouponEventService;
import com.onepage.coupong.sign.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponEventController {

    private static final Logger log = LoggerFactory.getLogger(CouponEventController.class);
    private final CouponEventService couponEventService;
    private final AuthService authService;

    @PostMapping("/attempt")
    public ResponseEntity<String> requestCoupon(
            @RequestBody UserRequestDto userRequestDto, @RequestHeader("Authorization") String token
    ) {
        /* 헤더로부터 받아온 JWT 토큰 정보로부터 유저 ID 반환 후 userRequestDto에 입력 */
        Long userId = authService.tokenDecryptionId(token);
        userRequestDto.setId(userId);

        boolean publishSuccess = couponEventService.addUserToQueue(userRequestDto);
        if (publishSuccess) {
            return ResponseEntity.ok("쿠폰 발급 요청에 성공했습니다 - 대기열 등록 -");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("쿠폰 발급 요청 실패");
        }
    }
}
