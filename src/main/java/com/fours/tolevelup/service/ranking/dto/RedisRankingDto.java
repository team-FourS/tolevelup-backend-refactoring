package com.fours.tolevelup.service.ranking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisRankingDto {
    private String userId;
    private Long ranking;
    private Double score;
}
