package com.fours.tolevelup.batch.mission.daily;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyMissionScheduler {

    private final JobLauncher jobLauncher;
    @Qualifier("dailyMissionAssignJob")
    private final Job dailyMissionAssignJob;

    @Scheduled(cron = "0 0 0 * * *")
    public void runWeeklyMissionJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(dailyMissionAssignJob, params);
    }

}
