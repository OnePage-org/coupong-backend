package com.onepage.coupong.presentation.leaderboard;

import com.onepage.coupong.global.presentation.CommonResponseEntity;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

import static com.onepage.coupong.global.presentation.CommonResponseEntity.success;
import static com.onepage.coupong.presentation.leaderboard.enums.LeaderboardResp.LEADERBOARD_CLEAR_SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardUseCase leaderboardUseCase;

    @GetMapping(value = "/sse/leaderboard/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLeaderboardUpdates() {
        return leaderboardUseCase.addSseEmitter();
    }

    @GetMapping("/sse/leaderboard")
    public Map<String, Map<Object, Double>> getLeaderboard(@RequestParam String couponCategory) {
        return leaderboardUseCase.getLeaderboard(couponCategory);
    }

    @PostMapping("/sse/leaderboard/clear")
    public CommonResponseEntity<String> clearLeaderboard(@RequestParam String couponCategory) {
        leaderboardUseCase.clearLeaderboard(couponCategory);
        return success(LEADERBOARD_CLEAR_SUCCESS.getMessage());
    }

    @GetMapping("/api/categories")
    public CommonResponseEntity<List<String>> getAllCategories() {
        List<String> categories = Arrays.stream(CouponCategory.values())
                .filter(category -> category != CouponCategory.DEFAULT)
                .map(Enum::name)
                .toList();
        return success(categories);
    }
}
