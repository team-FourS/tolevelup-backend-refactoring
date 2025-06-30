package com.fours.tolevelup.controller.response;


import com.fours.tolevelup.service.ranking.dto.UserThemeRankingDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThemeRankResponse {

    private List<UserThemeRankingDto> themeRankList;

    public static ThemeRankResponse of(List<UserThemeRankingDto> themeRankList) {
        return new ThemeRankResponse(themeRankList);
    }
}
