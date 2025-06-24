package com.fours.tolevelup.repository;

import com.fours.tolevelup.model.ThemeType;
import com.fours.tolevelup.model.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Optional<Theme> findById(int id);

    List<Theme> findAllByType(ThemeType type);

}
