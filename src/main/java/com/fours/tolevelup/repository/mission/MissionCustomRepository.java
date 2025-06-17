package com.fours.tolevelup.repository.mission;

import com.fours.tolevelup.model.entity.Mission;

import java.util.List;

public interface MissionCustomRepository {

    Mission findByContent(String content);
    List<Mission> findByTheme(int theme_id);

}
