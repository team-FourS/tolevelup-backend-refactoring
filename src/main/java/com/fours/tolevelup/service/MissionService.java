package com.fours.tolevelup.service;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.service.dto.MissionDTO;
import com.fours.tolevelup.model.entity.Character;
import com.fours.tolevelup.model.entity.Mission;
import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import com.fours.tolevelup.repository.character.CharacterRepository;
import com.fours.tolevelup.repository.character.UserCharacterRepository;
import com.fours.tolevelup.repository.MissionRepository;
import com.fours.tolevelup.repository.MissionLogRepository;
import com.fours.tolevelup.repository.ThemeExpRepository;
import com.fours.tolevelup.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class MissionService {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final MissionRepository missionRepository;
    private final MissionLogRepository missionLogRepository;
    private final ThemeExpRepository themeExpRepository;
    private final CharacterRepository characterRepository;


    public List<MissionDTO.mission> userToDayCompleteList(String userId) {
        return missionLogRepository.findTodayCompleteByUser(userId)
                .stream().map(MissionDTO.mission::fromMissionLog).collect(Collectors.toList());
    }

    // 해당 로직 UserMissionService 로 리팩터링했습니다! 캐릭터 부분 있어서 남겨두었습니다!
    @Transactional
    public void changeMissionStatus(String userId, int missionId, Long logId) {
        User user = getUserOrException(userId);
        Mission mission = getMissionOrException(missionId);
        MissionLog missionLog = getMissionLogOrException(logId);

        UserCharacter userCharacter = userCharacterRepository.findUserCharacterByUserAndThemeName(user,mission.getTheme().getName());
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

    private MissionLog getMissionLogOrException(Long missionLogId) {
        return missionLogRepository.findById(missionLogId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.MISSION_LOG_NOT_FOUND));
    }

    private float getMissionExp(MissionLog missionLog) {
        if (checkComplete(missionLog)) {
            return missionLog.getMission().getExp() * -1;
        }
        return missionLog.getMission().getExp();
    }

    private boolean checkComplete(MissionLog missionLog) {
        return missionLog.getStatus().toString().split("_")[1].equals("COMPLETE");
    }



}
