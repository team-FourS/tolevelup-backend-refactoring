package com.fours.tolevelup.service.ranking.dto;


import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.service.dto.UserDTO.publicUserData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRankingDto {
    private publicUserData userData;
    private Double exp_total;
    private Long rank;

    public static UserRankingDto of(User user, Double exp_total, Long rank) {
        return new UserRankingDto(
                publicUserData.fromUser(user),
                exp_total,
                rank
        );
    }
}
