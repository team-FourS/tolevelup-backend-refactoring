package com.fours.tolevelup.service.ranking;


import com.fours.tolevelup.service.ranking.dto.RedisRankingDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
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

    public List<RedisRankingDto> getRankListBy(int start, int size) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> range = zSetOps
                .reverseRangeWithScores(BASIC_RANK, start, start + size);
        return getRankList(start, range);
    }

    public List<RedisRankingDto> getThemeRankListBy(int themeId, int start, int size) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> range = zSetOps
                .reverseRangeWithScores(String.format(THEME_RANK, themeId), start, start + size);
        return getRankList(size, range);
    }

    private List<RedisRankingDto> getRankList(
            int start, Set<ZSetOperations.TypedTuple<String>> range
    ) {
        if (range == null || range.isEmpty()) {
            return List.of();
        }
        List<RedisRankingDto> rankingList = new ArrayList<>();
        long rank = start;
        for(ZSetOperations.TypedTuple<String> tuple : range) {
            rankingList.add(new RedisRankingDto(
                    tuple.getValue(),
                    rank++,
                    tuple.getScore()
            ));
        }
        return rankingList;
    }

}


