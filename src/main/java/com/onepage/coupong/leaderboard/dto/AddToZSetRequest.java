package com.onepage.coupong.leaderboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToZSetRequest {
    private String couponCategory;
    private String userId;
    private double attemptAt;
}
