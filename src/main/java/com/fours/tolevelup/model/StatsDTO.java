package com.fours.tolevelup.model;


import com.fours.tolevelup.model.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatsDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ThemeCompleteCounts{
        String themeName;
        long completeCounts;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ThemeExps{
        String themeName;
        long expCounts;
    }
}
