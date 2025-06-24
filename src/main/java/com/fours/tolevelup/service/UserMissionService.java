package com.fours.tolevelup.service;


import com.fours.tolevelup.controller.response.UserThemeMissionResponse;
import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.MissionStatus;
import com.fours.tolevelup.model.ThemeType;
import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.MissionLogRepository;
import com.fours.tolevelup.repository.ThemeRepository;
import com.fours.tolevelup.repository.UserRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserMissionService {

    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final MissionLogRepository missionLogRepository;

    @Transactional(readOnly = true)
    public List<UserThemeMissionResponse> getTodayMissionWithTheme(String userId, int themeId) {
        User user = getUserOrException(userId);
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new TluApplicationException(ErrorCode.THEME_NOT_FOUND));
        return missionLogRepository
                .findUserTodayMissionByTheme(user, theme, getStartDate(theme.getType()))
                .stream()
                .map(UserThemeMissionResponse::fromMissionLog)
                .toList();
    }

    @Transactional
    public void changeStatus(String userId, Long missionLodId) {
        MissionLog missionLog = getMissionLogOrException(missionLodId);
        userRightCheck(userId, missionLog.getUser());
        missionLog.updateStatus(getOppositeStatus(missionLog.getStatus()));
    }

    private Date getStartDate(ThemeType themeType) {
        if (themeType == ThemeType.WEEKLY) {
            return Date.valueOf(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1));
        }
        return Date.valueOf(LocalDate.now());
    }

    private MissionStatus getOppositeStatus(MissionStatus status) {
        return switch (status) {
            case DAILY_COMPLETE -> MissionStatus.DAILY_ONGOING;
            case WEEKLY_COMPLETE -> MissionStatus.WEEKLY_ONGOING;
            case DAILY_ONGOING -> MissionStatus.DAILY_COMPLETE;
            case WEEKLY_ONGOING -> MissionStatus.WEEKLY_COMPLETE;
        };
    }

    private void userRightCheck(String user, User checkUser) {
        if (!user.equals(checkUser.getId())) {
            throw new TluApplicationException(ErrorCode.INVALID_PERMISSION);
        }
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    private MissionLog getMissionLogOrException(Long missionLogId) {
        return missionLogRepository.findById(missionLogId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.MISSION_LOG_NOT_FOUND));
    }
}





