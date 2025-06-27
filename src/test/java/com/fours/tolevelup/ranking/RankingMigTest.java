package com.fours.tolevelup.ranking;


import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.UserRepository;
import com.fours.tolevelup.service.ranking.RankingMigrationScheduler;
import com.fours.tolevelup.service.ranking.RankingRedisService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RankingMigTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankingRedisService rankingRedisService;

    @Autowired
    private RankingMigrationScheduler scheduler;

    @Test
    void test() {
        scheduler.migrateToDB();
    }

    @Test
    void generateTestUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 100; i < 120; i++) {
            String id = String.valueOf(i);
            users.add(User.builder()
                    .id(id)
                    .name("유저"+i)
                    .level(0)
                    .intro("안녕하세요")
                    .password("123456")
                    .email(i+"testuser@naver.com")
                    .build());
            rankingRedisService.updateUserExp(id, 1, i);
        }
        userRepository.saveAll(users);
    }
}
