package com.fours.tolevelup.service.ranking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

@Getter
@AllArgsConstructor
public class RedisRankingDto {
    private String userId;
    private Long ranking;
    private Double score;

    public static RedisRankingDto of(TypedTuple<String> tuple, Long ranking) {
        return new RedisRankingDto(
                tuple.getValue(),
                ranking,
                tuple.getScore()
        );
    }
}
