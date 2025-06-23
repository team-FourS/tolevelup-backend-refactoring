package com.fours.tolevelup.service.missionlog;

import com.fours.tolevelup.model.MissionStatus;
import com.fours.tolevelup.model.entity.Mission;
import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.mission.MissionRepository;
import com.fours.tolevelup.repository.missionlog.MissionLogRepository;
import com.fours.tolevelup.repository.theme.ThemeRepositoryImpl;
import com.fours.tolevelup.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionLogService {

    private final MissionLogRepository missionLogRepository;
    private final MissionRepository missionRepository;
    private final ThemeRepositoryImpl themeRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void dailyMissionLogControl() {
        List<User> userList = userRepository.findAll();
        for (User u : userList) {
            insertLog(u, randomMission("daily"), MissionStatus.DAILY_ONGOING);
        }
    }

    @Scheduled(cron = "1 0 0 * * 1")
    @Transactional
    public void weeklyMissionLogControl() {
        deleteLog(MissionStatus.WEEKLY_ONGOING);
        List<User> userList = userRepository.findAll();
        for (User u : userList) {
            insertLog(u, randomMission("weekly"), MissionStatus.WEEKLY_ONGOING);
        }
    }

    @Transactional
    public void createMissionLog(User u) {
        insertLog(u, randomMission("daily"), MissionStatus.DAILY_ONGOING);
        insertLog(u, randomMission("weekly"), MissionStatus.WEEKLY_ONGOING);
    }

    private void deleteLog(MissionStatus status) {
        List<MissionLog> missionLogList = missionLogRepository.findByStatus(status);
        missionLogRepository.deleteAll(missionLogList);
    }

    public List<MissionLog> assignDailyMissions(User user) {
        List<Theme> themeList = themeRepository.findByType("daily");
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
        List<Theme> themeList = themeRepository.findByType("weekly");
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


    private void insertLog(User u, List<Mission> missionList, MissionStatus status) {
        for (Mission m : missionList) {
            MissionLog log = MissionLog.builder().user(u).mission(m).status(status).build();
            missionLogRepository.saveMissionLog(log);
        }
    }

    private List<Mission> randomMission(String type) {
        List<Theme> themeList = themeRepository.findByType(type);
        List<Mission> randomMissionList = new ArrayList<>();
        for (Theme theme : themeList) {
            List<Mission> missionList = missionRepository.findByTheme(theme.getId());
            Collections.shuffle(missionList);
            missionList = missionList.subList(0, type.equals("daily") ? 3 : 2);
            randomMissionList.addAll(missionList);
        }
        return randomMissionList;
    }


}
