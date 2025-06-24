package com.fours.tolevelup.service;

import com.fours.tolevelup.model.MissionStatus;
import com.fours.tolevelup.model.ThemeType;
import com.fours.tolevelup.model.entity.Mission;
import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.MissionRepository;
import com.fours.tolevelup.repository.ThemeRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MissionLogService {

    private final MissionRepository missionRepository;
    private final ThemeRepository themeRepository;


    public List<MissionLog> assignDailyMissions(User user) {
        List<Theme> themeList = themeRepository.findAllByType(ThemeType.DAILY);
        List<Mission> randomMissionList = new ArrayList<>();

        for (Theme theme : themeList) {
            List<Integer> missionIds = missionRepository.findMissionIdsByThemeId(theme.getId());
            Collections.shuffle(missionIds);
            List<Integer> selectedIds = missionIds.subList(0, 3);
            List<Mission> selectedMissions = new ArrayList<>();
            for(int id : selectedIds) {
                selectedMissions.add(missionRepository.findAllById(id));
            }
            randomMissionList.addAll(selectedMissions);
        }

        List<MissionLog> logs = new ArrayList<>();
        for (Mission mission : randomMissionList) {
            logs.add(MissionLog.builder()
                    .user(user)
                    .mission(mission)
                    .status(MissionStatus.DAILY_ONGOING)
                    .build());
        }
        return logs;
    }

    public List<MissionLog> assignWeeklyMissions(User user) {
        List<Theme> themeList = themeRepository.findAllByType(ThemeType.WEEKLY);
        List<Mission> randomMissionList = new ArrayList<>();

        for (Theme theme : themeList) {
            List<Integer> missionIds = missionRepository.findMissionIdsByThemeId(theme.getId());
            Collections.shuffle(missionIds);
            List<Integer> selectedIds = missionIds.subList(0, 2);
            List<Mission> selectedMissions = new ArrayList<>();
            for(int id : selectedIds) {
                selectedMissions.add(missionRepository.findAllById(id));
            }
            randomMissionList.addAll(selectedMissions);
        }

        List<MissionLog> logs = new ArrayList<>();
        for (Mission mission : randomMissionList) {
            logs.add(MissionLog.builder()
                    .user(user)
                    .mission(mission)
                    .status(MissionStatus.WEEKLY_ONGOING)
                    .build());
        }
        return logs;
    }

}
