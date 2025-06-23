package com.fours.tolevelup.batch.mission.weekly;


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
public class WeeklyMissionScheduler {

    private final JobLauncher jobLauncher;
    @Qualifier("weeklyMissionAssignJob")
    private final Job weeklyMissionAssignJob;

    @Scheduled(cron = "0 0 0 * * MON")
    public void runWeeklyMissionJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(weeklyMissionAssignJob, params);
    }

}
