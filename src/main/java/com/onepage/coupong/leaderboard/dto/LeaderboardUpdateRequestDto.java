package com.onepage.coupong.leaderboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaderboardUpdateRequestDto {
    private String couponCategory;
    private String userId;
    private double attemptAt;
}
