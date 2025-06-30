package com.fours.tolevelup.service.ranking.dto;


import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserThemeRanking;
import com.fours.tolevelup.service.dto.UserDTO.publicUserData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserThemeRankingDto {
    private publicUserData userData;
    private int themeId;
    private Double totalExp;
    private Long rank;

    public static UserThemeRankingDto fromEntity(UserThemeRanking userThemeRanking) {
        return new UserThemeRankingDto(
                publicUserData.fromUser(userThemeRanking.getUser()),
                userThemeRanking.getTheme().getId(),
                userThemeRanking.getTotalExp(),
                userThemeRanking.getRanking()
        );
    }

    public static UserThemeRankingDto fromRedis(User user, int themeId, RedisRankingDto redisRankingDto) {
        return new UserThemeRankingDto(
                publicUserData.fromUser(user),
                themeId,
                redisRankingDto.getScore(),
                redisRankingDto.getRanking()
        );
    }
}


