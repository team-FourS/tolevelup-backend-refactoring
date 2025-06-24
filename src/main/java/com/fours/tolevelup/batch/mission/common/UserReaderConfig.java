package com.fours.tolevelup.batch.mission.common;


import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

@Configuration
@RequiredArgsConstructor
public class UserReaderConfig {

    private final UserRepository userRepository;

    @Bean
    @StepScope
    public RepositoryItemReader<User> userItemReader() {
        RepositoryItemReader<User> reader = new RepositoryItemReader<>();
        reader.setRepository(userRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(100);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }
}
