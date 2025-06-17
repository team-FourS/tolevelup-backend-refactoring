package com.fours.tolevelup.repository.themeexp;

import com.fours.tolevelup.model.entity.ThemeExp;

import java.util.*;

public interface ThemeExpCustomRepository {
    void save1(ThemeExp themeExp);

//    List<ThemeExp> findById(String id);
    List<ThemeExp> findByUser_id(String user_id);

    List<ThemeExp> findThemeExp(String user_id);
    void expPlus(float exp_total, String user_id, int theme_id);
    void expMinus(float exp_total, String user_id, int theme_id);

}
