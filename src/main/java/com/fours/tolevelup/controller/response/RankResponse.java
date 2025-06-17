package com.fours.tolevelup.controller.response;

import com.fours.tolevelup.model.RankDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class RankResponse {
    @Getter
    @AllArgsConstructor
    public static class RankList{
        private List<RankDTO.RankData> rankList;
    }

    @Getter
    @AllArgsConstructor
    public static class ThemeRankList{
        private List<RankDTO.themeRankData> themeRankDataList;
    }

}
