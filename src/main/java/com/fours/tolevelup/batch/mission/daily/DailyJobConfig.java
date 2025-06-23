package com.fours.tolevelup.batch.mission.daily;


import com.fours.tolevelup.batch.mission.common.LogFlatteningWriter;
import com.fours.tolevelup.batch.mission.common.MissionLogWriter;
import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.model.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class DailyJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final RepositoryItemReader<User> userItemReader;
    private final DailyMissionProcessor dailyMissionProcessor;
    private final MissionLogWriter logWriter;

    @Bean(name = "dailyMissionAssignJob")
    public Job dailyMissionAssignJob() {
        return jobBuilderFactory.get("dailyMissionAssignJob")
                .start(assignDailyMissionStep())
                .build();
    }

    @Bean
    public Step assignDailyMissionStep() {
        return stepBuilderFactory.get("assignDailyMissionStep")
                .<User, List<MissionLog>>chunk(100)
                .reader(userItemReader)
                .processor(dailyMissionProcessor)
                .writer(dailyFlatteningWriter())
                .faultTolerant()
                .retryLimit(30)
                .retry(Exception.class)
                .skipLimit(100)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public ItemWriter<List<MissionLog>> dailyFlatteningWriter() {
        return new LogFlatteningWriter(logWriter);
    }

}


