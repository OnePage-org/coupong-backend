package com.onepage.coupong.coupon.api;

import com.onepage.coupong.coupon.dto.CouponEventListDto;
import com.onepage.coupong.coupon.dto.UserRequestDto;
import com.onepage.coupong.coupon.domain.Coupon;
import com.onepage.coupong.coupon.domain.EventManager;
import com.onepage.coupong.coupon.domain.enums.CouponCategory;
import com.onepage.coupong.coupon.exception.EventException;
import com.onepage.coupong.coupon.service.CouponEventService;
import com.onepage.coupong.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/coupon-event")
@RequiredArgsConstructor
public class CouponEventController {

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

    @GetMapping("/list")
    public ResponseEntity<List<CouponEventListDto>> getCouponEventList() {
        log.info("이벤트 목록 요청 들어옴");

        // 현재 초기화된 모든 이벤트의 카테고리별 EventManager 가져오기
        Map<CouponCategory, EventManager> activeEvents = couponEventService.getAllInitializedEvents();

        if (activeEvents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 현재 초기화된 이벤트가 없을 경우
        }

        // 각 이벤트 정보를 CouponEventListDto로 변환하여 리스트에 추가
        List<CouponEventListDto> eventListDto = new ArrayList<>();
        for (Map.Entry<CouponCategory, EventManager> entry : activeEvents.entrySet()) {
            EventManager eventManager = entry.getValue();

            /*// 대기열에 데이터가 남아 있으면 해당 카테고리 이벤트는 제외 (이벤트 도중에는 에러를 일으키는 코드)
            if (!couponEventService.getIssuanceQueue(String.valueOf(eventManager.getCouponCategory())).isEmpty() ||
                    !couponEventService.getLeaderBoardQueue(String.valueOf(eventManager.getCouponCategory())).isEmpty()) {
                log.warn("이벤트 진행 중: 카테고리 = {}", eventManager.getCouponCategory());
                continue;  // 대기열에 데이터가 남아 있으면 건너뜀
            }*/

            // 이벤트 정보 DTO 변환
            CouponEventListDto eventDto = CouponEventListDto.builder()
                    .eventName(eventManager.getCouponName())
                    .eventCategory(String.valueOf(eventManager.getCouponCategory()))
                    .startTime(eventManager.getStartTime())
                    .build();

            eventListDto.add(eventDto);
        }

        // 이벤트 목록 반환
        return new ResponseEntity<>(eventListDto, HttpStatus.OK);
    }


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
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
        }
    }
}
