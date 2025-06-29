package com.fours.tolevelup.service.ranking;


import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.repository.ThemeRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankingMigrationScheduler {
    private final ThemeRepository themeRepository;
    private final RankingRedisService rankingRedisService;
    private final RankingMigrationService rankingMigrationService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void migrateToDB() {
        LocalDate now = LocalDate.now().minusMonths(1);
        int year = now.getYear();
        int month = now.getMonthValue();

        rankingMigrationService.migrateToUserRanking(year, month);

        List<Theme> themes = themeRepository.findAll();
        for(Theme theme : themes) {
            rankingMigrationService.migrateToUserThemeRanking(theme, year, month);
        }

        rankingRedisService.deleteKeys();
    }

}
