package com.fours.tolevelup.batch.mission.common;


import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.repository.MissionLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionLogWriter implements ItemWriter<MissionLog> {

    private final MissionLogRepository missionLogRepository;

    @Override
    public void write(List<? extends MissionLog> items) {
        missionLogRepository.saveAll(items);
    }
}
