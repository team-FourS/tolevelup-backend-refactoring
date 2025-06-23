package com.fours.tolevelup.missionlog;



import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
public class MissionBatchPerformanceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("dailyMissionAssignJob")
    private Job dailyMissionAssignJob;


    @DisplayName("배치 성능 확인")
    @Test
    void testMissionInsertPerformance() throws Exception {

        /*
        기존 서비스 메서드 실행 시간 측정
        long startOld = System.currentTimeMillis();
        missionlogService.dailyMissionLogControl();
        long endOld = System.currentTimeMillis();
        long durationOld = endOld - startOld;
        */


        // Spring Batch Job 실행 시간 측정
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("runDate", new Date())
                .toJobParameters();

        long startBatch = System.currentTimeMillis();
        JobExecution jobExecution = jobLauncher.run(dailyMissionAssignJob, jobParameters);
        waitJobDone(jobExecution);
        long endBatch = System.currentTimeMillis();
        long durationBatch = endBatch - startBatch;

        //log.info("[기존 방식 실행 시간] {} ms", durationOld);
        log.info("[배치 방식 실행 시간] {} ms", durationBatch);

        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

    }

    private void waitJobDone(JobExecution jobExecution) throws InterruptedException {
        while (jobExecution.getStatus() != BatchStatus.COMPLETED) {
            Thread.sleep(100);
        }
    }

    private void generateTestUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(User.builder()
                            .id(i+"유저")
                            .name("유저"+i)
                            .level(0)
                            .intro("안녕하세요")
                            .password("123456")
                            .email(i+"testuser@naver.com")
                    .build());
        }
        userRepository.saveAll(users);
    }

}


