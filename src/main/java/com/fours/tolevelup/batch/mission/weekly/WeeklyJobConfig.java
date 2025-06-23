package com.fours.tolevelup.batch.mission.weekly;


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
public class WeeklyJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final RepositoryItemReader<User> userItemReader;
    private final WeeklyMissionProcessor weeklyMissionProcessor;
    private final MissionLogWriter logWriter;


    @Bean(name = "weeklyMissionAssignJob")
    public Job weeklyMissionAssignJob() {
        return jobBuilderFactory.get("weeklyMissionAssignJob")
                .start(assignWeeklyMissionStep())
                .build();
    }

    @Bean
    public Step assignWeeklyMissionStep() {
        return stepBuilderFactory.get("assignWeeklyMissionStep")
                .<User, List<MissionLog>>chunk(100)
                .reader(userItemReader)
                .processor(weeklyMissionProcessor)
                .writer(weeklyFlatteningWriter())
                .build();
    }

    @Bean
    public ItemWriter<List<MissionLog>> weeklyFlatteningWriter() {
        return new LogFlatteningWriter(logWriter);
    }
}
