package com.fours.tolevelup.service.ranking;


import com.fours.tolevelup.service.ranking.dto.RedisRankingDto;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingRedisService {

    private static final String BASIC_RANK = "rank:basic";
    private static final String THEME_RANK = "rank:theme:%d";

    private final StringRedisTemplate redisTemplate;

    public void updateUserExp(String userId, int themeId, int exp) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(BASIC_RANK, userId, exp);
        zSetOps.incrementScore(String.format(THEME_RANK, themeId), userId, exp);
    }

    public Long getRank(String userId) {
        return redisTemplate.opsForZSet().reverseRank(userId, BASIC_RANK);
    }

    public Long getThemeRank(String userId, int themeId) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRank(String.format(THEME_RANK, themeId), userId);
    }

    public List<RedisRankingDto> getRankingList(int start, int end) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<TypedTuple<String>> range = zSetOps.reverseRangeWithScores(BASIC_RANK, start, end);
        return (range == null || range.isEmpty())
                ? List.of()
                : range.stream().map(tuple ->
                        RedisRankingDto.of(tuple, getRank(tuple.getValue()))
                ).toList();
    }

    public List<RedisRankingDto> getThemeRankingList(int themeId, int start, int end) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<TypedTuple<String>> range = zSetOps.reverseRangeWithScores(String.format(THEME_RANK, themeId), start, end);
        return (range == null || range.isEmpty())
                ? List.of()
                : range.stream().map(tuple ->
                        RedisRankingDto.of(tuple, getThemeRank(tuple.getValue(), themeId))
                ).toList();
    }

    public void deleteKeys() {
        Set<String> keys = redisTemplate.keys("rank:*");
        if(keys == null || keys.isEmpty()) {
            return;
        }
        redisTemplate.delete(keys);
    }

}

