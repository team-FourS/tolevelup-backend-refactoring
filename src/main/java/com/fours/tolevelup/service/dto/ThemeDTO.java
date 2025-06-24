package com.fours.tolevelup.service.dto;

import com.fours.tolevelup.model.ThemeType;
import com.fours.tolevelup.model.entity.Theme;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ThemeDTO {
    private int id;
    private String name;
    private ThemeType type;

    public static ThemeDTO from(Theme theme) {
        return new ThemeDTO(
                theme.getId(),
                theme.getName(),
                theme.getType()
        );
    }
}
