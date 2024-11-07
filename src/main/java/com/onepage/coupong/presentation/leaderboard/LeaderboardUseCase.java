package com.onepage.coupong.presentation.leaderboard;

import java.util.Set;

public interface LeaderboardUseCase {
    public void updateLeaderboard(String couponCategory, Set<Object> topWinners, double entryTime);

    public String createLeaderboardUpdateMessage(String couponCategory, Set<Object> topWinners, double entryTime);
}
