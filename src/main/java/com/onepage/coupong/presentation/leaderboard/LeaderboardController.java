package com.onepage.coupong.presentation.leaderboard;

import com.onepage.coupong.business.leaderboard.dto.CategoryDto;
import com.onepage.coupong.business.leaderboard.dto.LeaderboardDto;
import com.onepage.coupong.business.leaderboard.dto.SseEmitterDto;
import com.onepage.coupong.global.presentation.CommonResponseEntity;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.onepage.coupong.global.presentation.CommonResponseEntity.success;
import static com.onepage.coupong.presentation.leaderboard.enums.LeaderboardResp.LEADERBOARD_CLEAR_SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardUseCase leaderboardUseCase;

    @GetMapping(value = "/sse/leaderboard/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public CommonResponseEntity<SseEmitterDto> streamLeaderboardUpdates() {
        SseEmitterDto dto = leaderboardUseCase.addSseEmitter();
        return success(dto);
    }

    @GetMapping("/sse/leaderboard")
    public CommonResponseEntity<LeaderboardDto> getLeaderboard(@RequestParam String couponCategory) {
        LeaderboardDto dto = leaderboardUseCase.getLeaderboard(couponCategory);
        return success(dto);
    }

    @PostMapping("/sse/leaderboard/clear")
    public CommonResponseEntity<String> clearLeaderboard(@RequestParam String couponCategory) {
        leaderboardUseCase.clearLeaderboard(couponCategory);
        return success(LEADERBOARD_CLEAR_SUCCESS.getMessage());
    }

    @GetMapping("/api/categories")
    public CommonResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> dtos = leaderboardUseCase.getAllCategories();
        return success(dtos);
    }
}
