package com.fours.tolevelup.service.ranking.dto;


import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.service.dto.UserDTO.publicUserData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserThemeRankingDto {
    private publicUserData userData;
    private int themeId;
    private Double exp_total;
    private Long rank;

    public static UserThemeRankingDto of(
            User user, int themeId, Double exp_total, Long rank
    ) {
        return new UserThemeRankingDto(
                publicUserData.fromUser(user),
                themeId,
                exp_total,
                rank
        );
    }
}













