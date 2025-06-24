package com.fours.tolevelup.batch.mission.common;


import com.fours.tolevelup.model.entity.MissionLog;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;


@RequiredArgsConstructor
public class LogFlatteningWriter implements ItemWriter<List<MissionLog>> {

    private final ItemWriter<MissionLog> delegate;

    @Override
    public void write(List<? extends List<MissionLog>> items) throws Exception {
        for (List<MissionLog> logs : items) {
            delegate.write(logs);
        }
    }
}
