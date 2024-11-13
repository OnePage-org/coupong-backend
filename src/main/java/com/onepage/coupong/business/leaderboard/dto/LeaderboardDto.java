package com.onepage.coupong.business.leaderboard.dto;

import java.util.Map;

public record LeaderboardDto(Map<String, Map<Object, Double>> leaderboard) {
}