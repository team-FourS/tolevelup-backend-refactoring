package com.fours.tolevelup.service.dto;

import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.ThemeExp;
import com.fours.tolevelup.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ThemeExpDTO {

    private User user;
    private Theme theme;
    private float exp_total;

    public static ThemeExpDTO fromEntity(ThemeExp entity){
        return new ThemeExpDTO(
                entity.getUser(),
                entity.getTheme(),
                entity.getExp_total()
        );
    }



}
