package com.fours.tolevelup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class RankData{
        private UserDTO.publicUserData userData;
        private Integer exp_total;
        private Integer rank;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class themeRankData{
        private UserDTO.publicUserData userData;
        private int themeId;
        private Long exp_total;
        private Integer rank;
    }



}
