package com.fours.tolevelup.service.ranking;

import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.entity.UserRanking;
import com.fours.tolevelup.model.entity.UserThemeRanking;
import com.fours.tolevelup.repository.UserRankingRepository;
import com.fours.tolevelup.repository.UserThemeRankingRepository;
import com.fours.tolevelup.service.dto.RankDTO;
import com.fours.tolevelup.service.dto.UserDTO;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.MissionLogRepository;
import com.fours.tolevelup.repository.ThemeRepository;
import com.fours.tolevelup.repository.ThemeExpRepository;
import com.fours.tolevelup.repository.UserRepository;
import com.fours.tolevelup.service.ranking.dto.UserRankingDto;
import com.fours.tolevelup.service.ranking.dto.UserThemeRankingDto;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RankService {
    private final UserRepository userRepository;
    private final ThemeExpRepository themeExpRepository;
    private final ThemeRepository themeRepository;
    private final MissionLogRepository missionLogRepository;

    private final RankingRedisService rankingRedisService;
    private final UserRankingRepository userRankingRepository;
    private final UserThemeRankingRepository themeRankingRepository;


    @Transactional(readOnly = true)
    public List<UserRankingDto> getRankingList(int year, int month, int start, int size) {
        if(isThisMonth(year, month)) {
            return rankingRedisService.getRankListBy(start, size).stream().map(dto ->
                    UserRankingDto.of(
                            getUserOrException(dto.getUserId()),
                            dto.getScore(),
                            dto.getRanking())).toList();
        }
        return getPastRanking(year, month, start, start + size);
    }

    @Transactional(readOnly = true)
    public List<UserThemeRankingDto> getThemeRankingList(
            int year, int month, int themeId, int start, int size
    ) {
        if(isThisMonth(year, month)) {
            return rankingRedisService.getThemeRankListBy(themeId, start, size).stream().map(dto ->
                    UserThemeRankingDto.of(
                            getUserOrException(dto.getUserId()),
                            themeId,
                            dto.getScore(),
                            dto.getRanking()
                    )).toList();
        }
        return getPastThemeRanking(year, month, themeId, start, start + size);
    }

    private List<UserRankingDto> getPastRanking(int year, int month, int start, int end) {
        Optional<List<UserRanking>> pastRankingList =
                userRankingRepository.findByRankingRange(year, month, start, end);
        return pastRankingList.map(userRankings -> userRankings.stream().map(dto ->
                UserRankingDto.of(
                        dto.getUser(),
                        dto.getTotalExp(),
                        dto.getRanking()
                )).toList()).orElse(List.of());
    }

    private List<UserThemeRankingDto> getPastThemeRanking(
            int themeId, int year, int month, int start, int end
    ) {
        Optional<List<UserThemeRanking>> pastRankingList =
                themeRankingRepository.findByRankingRange(themeId, year, month, start, end);
        return pastRankingList.map(userThemeRankings -> userThemeRankings.stream().map(dto ->
                UserThemeRankingDto.of(
                        dto.getUser(),
                        themeId,
                        dto.getTotalExp(),
                        dto.getRanking()
                )).toList()).orElseGet(List::of);
    }

    private boolean isThisMonth(int year, int month) {
        YearMonth nowMonth = YearMonth.now();
        return nowMonth.getYear() == year && nowMonth.getMonthValue() == month;
    }

    // 월별 totalTheme rank 정보
    public List<RankDTO.RankData> getRankTotalList(String userId, String date, Pageable pageable) {
        Slice<UserDTO.publicUserData> userList = themeExpRepository.findUserSortByUserId(pageable)
                .map(UserDTO.publicUserData::fromUser);
        List<RankDTO.RankData> rankList = new ArrayList<>();
        for (UserDTO.publicUserData user : userList) {
            rankList.add(RankDTO.RankData.builder().userData(user)
                    .exp_total(missionLogRepository.expTotal(user.getUserId(), date).orElseGet(() -> 0))
                    .rank(missionLogRepository.rank(date, user.getUserId()))
                    .build());
        }
        return rankList.stream().sorted(Comparator.comparing(RankDTO.RankData::getExp_total).reversed())
                .collect(Collectors.toList());
    }

    public List<RankDTO.themeRankData> getThemeRankList(String userId, int themeId, String date, Pageable pageable) {
        Slice<UserDTO.publicUserData> userList = themeExpRepository.findUserSortByUserId(pageable)
                .map(UserDTO.publicUserData::fromUser);
        Theme theme = getThemeOrException(themeId);
        User user = getUserOrException(userId);
        List<RankDTO.themeRankData> themeRankDataList = new ArrayList<>();
        for (UserDTO.publicUserData pUser : userList) {
            themeRankDataList.add(RankDTO.themeRankData.builder()
                    .userData(pUser)
                    .themeId(themeId)
                    .exp_total(missionLogRepository.expSumByDateAndThemeAndUserId(pUser.getUserId(), theme, date)
                            .orElseGet(() -> 0L))
                    .rank(missionLogRepository.themeRank(theme.getId(), date, pUser.getUserId()))
                    .build());
        }

        return themeRankDataList.stream().sorted(Comparator.comparing(RankDTO.themeRankData::getExp_total).reversed())
                .collect(Collectors.toList());
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
