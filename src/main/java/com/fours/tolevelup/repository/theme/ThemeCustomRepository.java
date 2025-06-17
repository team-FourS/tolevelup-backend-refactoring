package com.fours.tolevelup.repository.theme;


import com.fours.tolevelup.model.entity.Theme;

import java.util.List;

public interface ThemeCustomRepository {
    List<Theme> findAll();
    List<Theme> findByType(String type);
}
