package com.fours.tolevelup.service.mission;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.MissionDTO;
import com.fours.tolevelup.model.MissionStatus;
import com.fours.tolevelup.model.entity.Character;
import com.fours.tolevelup.model.entity.Mission;
import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import com.fours.tolevelup.repository.character.CharacterRepository;
import com.fours.tolevelup.repository.character.UserCharacterRepository;
import com.fours.tolevelup.repository.mission.MissionRepository;
import com.fours.tolevelup.repository.missionlog.MissionLogRepository;
import com.fours.tolevelup.repository.theme.ThemeRepository;
import com.fours.tolevelup.repository.themeexp.ThemeExpRepository;
import com.fours.tolevelup.repository.user.UserRepository;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final MissionRepository missionRepository;
    private final MissionLogRepository missionLogRepository;
    private final ThemeRepository themeRepository;
    private final ThemeExpRepository themeExpRepository;
    private final CharacterRepository characterRepository;


    public List<MissionDTO.mission> todayThemeMissions(String userId, int themeId) {
        User user = getUserOrException(userId);
        Theme theme = themeRepository.findById(themeId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.THEME_NOT_FOUND));
        Date startDate = getStartDate(theme.getType());
        System.out.println(startDate);
        return missionLogRepository.findByThemAndDate(user, theme, startDate).stream()
                .map(MissionDTO.mission::fromMissionLog)
                .collect(Collectors.toList());
    }

    public List<MissionDTO.mission> userToDayCompleteList(String userId) {
        return missionLogRepository.findTodayCompleteByUser(userId)
                .stream().map(MissionDTO.mission::fromMissionLog).collect(Collectors.toList());
    }


    @Transactional
    public void changeMissionStatus(String userId, int missionId) {
        User user = getUserOrException(userId);
        Mission mission = getMissionOrException(missionId);
        Date startDate = getStartDate(mission.getTheme().getType());
        MissionLog missionLog = getMissionLogOrException(user, mission, startDate);
        UserCharacter userCharacter = userCharacterRepository.findUserCharacterByUserAndThemeName(user,mission.getTheme().getName());
        missionLogRepository.updateMissionLogStatus(missionLog, changeStatus(missionLog), getEndTime(missionLog));
        int beforeExp = themeExpRepository.exp(user, mission.getTheme());
        themeExpRepository.updateExp(getMissionExp(missionLog), user, mission.getTheme());
        int afterExp = themeExpRepository.exp(user, mission.getTheme());

            if (afterExp / 10 > beforeExp / 10) {
                if(userCharacter.getCharacter().getLevel() < 4) {
                    levelUpCharacter(user, mission);
                }
            } else if (afterExp / 10 < beforeExp / 10) {
                levelDownCharacter(user, mission);
            }

    }

    private void levelUpCharacter(User user, Mission mission) {
        UserCharacter userCharacter = userCharacterRepository.findUserCharacterByUserAndThemeName(user,
                mission.getTheme().getName());
        Character updateCharacter = characterRepository.getLvUpCharacter(userCharacter.getCharacter().getLevel(),
                mission.getTheme().getId());
        userCharacterRepository.updateLevel(updateCharacter.getId(), userCharacter.getCharacter().getId());
    }

    private void levelDownCharacter(User user, Mission mission) {
        UserCharacter userCharacter = userCharacterRepository.findUserCharacterByUserAndThemeName(user,
                mission.getTheme().getName());
        Character updateCharacter = characterRepository.getLvDownCharacter(userCharacter.getCharacter().getLevel(),
                mission.getTheme().getId());
        userCharacterRepository.updateLevel(updateCharacter.getId(), userCharacter.getCharacter().getId());
    }


    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    private Mission getMissionOrException(int missionId) {
        return missionRepository.findById(missionId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.MISSION_NOT_FOUND));
    }

    private MissionLog getMissionLogOrException(User user, Mission mission, Date startDate) {
        return missionLogRepository.findByUserAndMission(user, mission, startDate).orElseThrow(() ->
                new TluApplicationException(ErrorCode.MISSION_LOG_NOT_FOUND));
    }

    private Date getStartDate(String themeType) {
        if (themeType.equals("weekly")) {
            return Date.valueOf(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1));
        }
        return Date.valueOf(LocalDate.now());
    }

    private float getMissionExp(MissionLog missionLog) {
        if (checkComplete(missionLog)) {
            return missionLog.getMission().getExp() * -1;
        }
        return missionLog.getMission().getExp();
    }

    private Timestamp getEndTime(MissionLog missionLog) {
        if (checkComplete(missionLog)) {
            return null;
        }
        return Timestamp.valueOf(LocalDateTime.now());
    }

    private boolean checkComplete(MissionLog missionLog) {
        return missionLog.getStatus().toString().split("_")[1].equals("COMPLETE");
    }

    private MissionStatus changeStatus(MissionLog missionLog) {
        switch (missionLog.getStatus()) {
            case DAILY_COMPLETE:
                return MissionStatus.DAILY_ONGOING;
            case WEEKLY_COMPLETE:
                return MissionStatus.WEEKLY_ONGOING;
            case DAILY_ONGOING:
                return MissionStatus.DAILY_COMPLETE;
            case WEEKLY_ONGOING:
                return MissionStatus.WEEKLY_COMPLETE;
        }
        throw new TluApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
