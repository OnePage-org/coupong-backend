package com.onepage.coupong.implementation.leaderboard.DtoMapper;

import com.onepage.coupong.business.leaderboard.dto.CategoryDto;
import com.onepage.coupong.business.leaderboard.dto.LeaderboardDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LeaderboardDtoMapper {
    public static LeaderboardDto toViewLeaderboard(Map<String, Map<Object, Double>> leaderboard) {
        return new LeaderboardDto(leaderboard);
    }

    public List<CategoryDto> toGetAllCategories(List<String> allCategories) {
        return allCategories.stream()
                .map(CategoryDto::new).collect(Collectors.toList());
    }
}
