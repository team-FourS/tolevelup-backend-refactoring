package com.fours.tolevelup.service.ranking;

import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.UserRankingRepository;
import com.fours.tolevelup.repository.UserRepository;
import com.fours.tolevelup.repository.UserThemeRankingRepository;
import com.fours.tolevelup.service.ranking.dto.UserRankingDto;
import com.fours.tolevelup.service.ranking.dto.UserThemeRankingDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UserRepository userRepository;
    private final RankingRedisService rankingRedisService;
    private final UserRankingRepository userRankingRepository;
    private final UserThemeRankingRepository themeRankingRepository;

    public List<UserRankingDto> getCurrentRankingList(int start, int end) {
        return rankingRedisService.getRankingList(start, end)
                .stream()
                .map(redisRankingDto ->
                        UserRankingDto.fromRedis(
                                getUserOrException(redisRankingDto.getUserId()), redisRankingDto))
                .toList();
    }

    public List<UserThemeRankingDto> getThemeCurrentRankingList(int start, int end, int themeId) {
        return rankingRedisService.getThemeRankingList(themeId, start, end)
                .stream()
                .map(redisRankingDto ->
                        UserThemeRankingDto.fromRedis(
                                getUserOrException(redisRankingDto.getUserId()),themeId,redisRankingDto))
                .toList();
    }

    @Transactional(readOnly = true)
    public Slice<UserRankingDto> getRankingList(int year, int month, Pageable pageable) {
        return userRankingRepository
                .findByYearAndMonth(year, month, pageable)
                .map(UserRankingDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public Slice<UserThemeRankingDto> getThemeRankingList(int year, int month, int themeId, Pageable pageable) {
        return themeRankingRepository
                .findByThemeIdAndYearAndMonth(themeId, year, month, pageable)
                .map(UserThemeRankingDto::fromEntity);
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", id))
        );
    }

}
