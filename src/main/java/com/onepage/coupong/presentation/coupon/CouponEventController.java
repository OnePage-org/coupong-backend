package com.onepage.coupong.presentation.coupon;

import com.onepage.coupong.business.coupon.dto.CouponEventListDto;
import com.onepage.coupong.business.coupon.dto.UserRequestDto;
import com.onepage.coupong.global.presentation.CommonResponseEntity;
import com.onepage.coupong.implementation.coupon.EventException;
import com.onepage.coupong.implementation.coupon.enums.EventExceptionType;
import com.onepage.coupong.presentation.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.onepage.coupong.global.presentation.CommonResponseEntity.error;
import static com.onepage.coupong.global.presentation.CommonResponseEntity.success;

import java.util.List;



@Slf4j
@RestController
@RequestMapping("/api/v1/coupon-event")
@RequiredArgsConstructor
public class CouponEventController {

    private final CouponEventUseCase couponEventUseCase;
    private final UserUseCase userUseCase;

    @GetMapping("/list")
    public CommonResponseEntity<List<CouponEventListDto>> getCouponEventList() {
        log.info("이벤트 목록 요청 들어옴");

        return success(couponEventUseCase.getCouponEventList());
    }


    @PostMapping("/attempt")
    public ResponseEntity<String> requestCoupon(
            @RequestBody UserRequestDto userRequestDto, @RequestHeader("Authorization") String token
    ) {
        /* 헤더로부터 받아온 JWT 토큰 정보로부터 유저 ID 반환 후 userRequestDto에 입력 */
        //Long userId = authService.tokenDecryptionId(token);
        //userRequestDto.setId(userId);

        try {
            boolean publishSuccess = couponEventUseCase.addUserToQueue(userRequestDto);
            if (publishSuccess) {

                log.info("대기열 등록 성공  요청 정보 ->"+ userRequestDto.getUsername() +" "+userRequestDto.getCouponCategory()+" "+userRequestDto.getAttemptAt());

                return ResponseEntity.ok("쿠폰 발급 요청에 성공했습니다 - 대기열 등록 -");
            } else {
                log.info("대기열 등록 실패 요청 정보 ->"+ userRequestDto.getUsername() +" "+userRequestDto.getCouponCategory()+" "+userRequestDto.getAttemptAt());

                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("쿠폰 발급 요청 실패");
            }
        } catch (IllegalStateException e) {
            log.info("대기열 등록 시스템 에러 발생 요청 정보(이벤트 초기화안됨) -> "+ userRequestDto.getUsername() +" "+userRequestDto.getCouponCategory()+" "+userRequestDto.getAttemptAt());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이벤트 오류: " + e.getMessage());
        } catch (EventException e) {
            log.info("쿠폰 이벤트 아직 시작 안함 -> "+userRequestDto);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이벤트 오류: " + e.getMessage());
        }
    }
}
