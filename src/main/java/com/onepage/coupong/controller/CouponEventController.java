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
@RequestMapping("/api/coupon-event")
@RequiredArgsConstructor
public class CouponEventController {

    private static final Logger log = LoggerFactory.getLogger(CouponEventController.class);
    private final CouponEventService couponEventService;
    private final AuthService authService;

    /*
    이벤트는 전역에서 관리할 수 있도록 ApplicationStartup 혹은 특정 관리자 인터페이스에서 관리하는 것이 일반적

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeEvent(@RequestBody EventInitRequestDto requestDto) {
        try {
            couponEventService.initializeEvent(requestDto.getCouponCategory(), requestDto.getCouponCount(), requestDto.getEndNums());
            return ResponseEntity.ok("이벤트 초기화 성공");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이벤트 초기화 실패: " + e.getMessage());
        }
    }*/

    @PostMapping("/attempt")
    public ResponseEntity<String> requestCoupon(
            @RequestBody UserRequestDto userRequestDto, @RequestHeader("Authorization") String token
    ) {
        /* 헤더로부터 받아온 JWT 토큰 정보로부터 유저 ID 반환 후 userRequestDto에 입력 */
        Long userId = authService.tokenDecryptionId(token);
        userRequestDto.setId(userId);

        try {
            boolean publishSuccess = couponEventService.addUserToQueue(userRequestDto);
            if (publishSuccess) {
                return ResponseEntity.ok("쿠폰 발급 요청에 성공했습니다 - 대기열 등록 -");
            } else {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("쿠폰 발급 요청 실패");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이벤트 오류: " + e.getMessage());
        }
    }

    /*
        @PostMapping("/stop")
    public ResponseEntity<String> stopEvent(
            @RequestHeader("Authorization") String token
    ) {
        // JWT 토큰 정보로 사용자 인증 및 권한 확인
        Long userId = authService.tokenDecryptionId(token);

        if (!couponEventService.isEventInitialized()) {
            return ResponseEntity.badRequest().body("진행 중인 이벤트가 없습니다.");
        }

        couponEventService.stopEvent();
        return ResponseEntity.ok("이벤트가 성공적으로 종료되었습니다.");
    }
     */
}
