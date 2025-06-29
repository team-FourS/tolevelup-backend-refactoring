package com.fours.tolevelup.service.ranking;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserRanking;
import com.fours.tolevelup.model.entity.UserThemeRanking;
import com.fours.tolevelup.repository.UserRankingRepository;
import com.fours.tolevelup.repository.UserRepository;
import com.fours.tolevelup.repository.UserThemeRankingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RankingMigrationService {

    private static final String BASIC_RANK = "rank:basic";
    private static final String THEME_RANK = "rank:theme:%d";

    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final UserRankingRepository userRankingRepository;
    private final UserThemeRankingRepository themeRankingRepository;

    private static final int MAX_RANKING = 10000;

    @Transactional
    public void migrateToUserRanking(int year, int month) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<TypedTuple<String>> range = zSetOps
                .reverseRangeWithScores(BASIC_RANK, 0, -1);

        if(range == null || range.isEmpty()) {
            return;
        }

        long rankSave = 1;
        long rankCount = 1;
        Double lastScore = null;

        List<UserRanking> rankings = new ArrayList<>();
        for(TypedTuple<String> tuple : range) {
            Double score = tuple.getScore();
            long ranking = (lastScore == null || lastScore.equals(score))
                    ? rankSave
                    : rankCount;
            if(ranking > MAX_RANKING) {
                break;
            }
            rankings.add(UserRanking.builder()
                            .user(getUserOrException(tuple.getValue()))
                            .year(year)
                            .month(month)
                            .ranking(ranking)
                            .totalExp(score)
                    .build()
            );
            lastScore = score;
            rankCount++;
        }
        userRankingRepository.saveAll(rankings);
    }

    @Transactional
    public void migrateToUserThemeRanking(Theme theme, int year, int month) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<TypedTuple<String>> range = zSetOps
                .reverseRangeWithScores(String.format(THEME_RANK, theme.getId()), 0, -1);

        if(range == null || range.isEmpty()) {
            return;
        }

        long rankSave = 1;
        long rankCount = 1;
        Double lastScore = null;

        List<UserThemeRanking> rankings = new ArrayList<>();
        for(TypedTuple<String> tuple : range) {
            Double score = tuple.getScore();
            long ranking = (lastScore == null || lastScore.equals(score))
                    ? rankSave
                    : rankCount;
            if(ranking > MAX_RANKING) {
                break;
            }
            rankings.add(UserThemeRanking.builder()
                            .user(getUserOrException(tuple.getValue()))
                            .year(year)
                            .month(month)
                            .theme(theme)
                            .totalExp(score)
                            .ranking(ranking)
                    .build()
            );
            lastScore = score;
            rankCount++;
        }
        themeRankingRepository.saveAll(rankings);
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND)
        );
    }

}





