package com.onepage.coupong.dto;

import java.util.List;

public class LeaderboardUpdateDTO {
    private String couponCategory;
    private List<String> winners;

    // 생성자
    public LeaderboardUpdateDTO(String couponCategory, List<String> winners) {
        this.couponCategory = couponCategory;
        this.winners = winners;
    }

    // Getter 및 Setter
    public String getCouponCategory() {
        return couponCategory;
    }

    public void setCouponCategory(String couponCategory) {
        this.couponCategory = couponCategory;
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }
}