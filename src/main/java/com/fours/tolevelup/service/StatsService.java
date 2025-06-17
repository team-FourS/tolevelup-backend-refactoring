package com.fours.tolevelup.service;

import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.StatsDTO;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.missionlog.MissionLogRepository;
import com.fours.tolevelup.repository.theme.ThemeRepository;
import com.fours.tolevelup.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final MissionLogRepository missionLogRepository;


    public long totalCompleteMissionCount(String userId) {
        User user = getUserOrException(userId);
        return missionLogRepository.countAllCompleteByUser(user);
    }

    public List<StatsDTO.ThemeCompleteCounts> completeThemeCount(String userId) {
        User user = getUserOrException(userId);
        List<Theme> themeList = themeRepository.findAll();
        List<StatsDTO.ThemeCompleteCounts> counts = new ArrayList<>();
        for (Theme t : themeList) {
            counts.add(new StatsDTO.ThemeCompleteCounts(t.getName(), themeCompletes(user, t)));
        }
        return counts;
    }

    public List<StatsDTO.ThemeExps> themeExps(String userId, String date) {
        User user = getUserOrException(userId);
        List<Theme> themeList = themeRepository.findAll();
        List<StatsDTO.ThemeExps> expList = new ArrayList<>();
        for (Theme t : themeList) {
            expList.add(new StatsDTO.ThemeExps(t.getName(),
                    missionLogRepository.expSumByDateAndTheme(user, t, date).orElseGet(() -> 0L)));
        }
        return expList;
    }


    public long themeDateExp(String userId, int themeId, String date) {
        User user = getUserOrException(userId);
        Theme theme = getThemeOrException(themeId);
        return missionLogRepository.expSumByDateAndTheme(user, theme, date).orElseGet(() -> 0L);
    }


    private long themeCompletes(User user, Theme theme) {
        return missionLogRepository.countByTheme(user, theme);
    }


    private Theme getThemeOrException(int themeId) {
        return themeRepository.findById(themeId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.THEME_NOT_FOUND));
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("%s is duplicated and c check", id)));
    }
}
