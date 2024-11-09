package com.onepage.coupong.presentation.coupon;

import com.onepage.coupong.business.coupon.dto.EventAttemptDto;
import com.onepage.coupong.business.coupon.dto.CouponEventDto;
import com.onepage.coupong.global.presentation.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.onepage.coupong.global.presentation.CommonResponseEntity.success;
import static com.onepage.coupong.presentation.coupon.enums.EventControllerResp.EVENT_ATTEMPT_SUCCESS;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/coupon-event")
@RequiredArgsConstructor
public class CouponEventController {

    private final CouponEventUseCase couponEventUseCase;

    @GetMapping("/list")
    public CommonResponseEntity<List<CouponEventDto>> getCouponEventList() {
        return success(couponEventUseCase.getCouponEventList());
    }

    @PostMapping("/attempt")
    public CommonResponseEntity<String> requestCoupon(@RequestBody EventAttemptDto eventAttemptDto) {
        couponEventUseCase.addUserToQueue(eventAttemptDto);
        return success(EVENT_ATTEMPT_SUCCESS.getMessage());
    }
}
