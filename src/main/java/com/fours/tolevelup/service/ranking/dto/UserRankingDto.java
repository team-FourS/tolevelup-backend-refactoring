package com.fours.tolevelup.service.ranking.dto;


import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserRanking;
import com.fours.tolevelup.service.dto.UserDTO.publicUserData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRankingDto {
    private publicUserData userData;
    private Double totalExp;
    private Long rank;

    public static UserRankingDto fromEntity(UserRanking userRanking) {
        return new UserRankingDto(
                publicUserData.fromUser(userRanking.getUser()),
                userRanking.getTotalExp(),
                userRanking.getRanking()
        );
    }

    public static UserRankingDto fromRedis(User user, RedisRankingDto redisRankingDto) {
        return new UserRankingDto(
                publicUserData.fromUser(user),
                redisRankingDto.getScore(),
                redisRankingDto.getRanking()
        );
    }
}

