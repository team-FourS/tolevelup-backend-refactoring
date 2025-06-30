package com.fours.tolevelup.ranking;

import com.fours.tolevelup.model.entity.UserThemeRanking;
import com.fours.tolevelup.repository.UserThemeRankingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RankingPerformanceTest {

    @Autowired
    private UserThemeRankingRepository themeRankingRepository;


    @DisplayName("유저 100명 랭킹 조회시의 성능 테스트")
    @Test
    void queryPerformanceTest() {
        List<Long> newRank = new ArrayList<>();
        long startNew = System.currentTimeMillis();
        for(int i=1000;i<=1100;i++) {
            Optional<UserThemeRanking> ranking = themeRankingRepository
                    .findByUserIdAndThemeIdAndYearAndMonth(i+"유저", 1, 2025, 6);
            newRank.add(ranking.map(UserThemeRanking::getRanking).orElse(10000L));
        }
        long endNew = System.currentTimeMillis();
        long durationNew = endNew - startNew;
        log.info("[변경 방식 실행 시간] {} ms", durationNew);
        newRank = null;
    }

}

