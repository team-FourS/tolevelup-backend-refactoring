package com.fours.tolevelup.controller.response;


import com.fours.tolevelup.service.ranking.dto.UserRankingDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankResponse {

    private List<UserRankingDto> rankList;

    public static RankResponse of(List<UserRankingDto> userRankingDto) {
        return new RankResponse(userRankingDto);
    }
}
