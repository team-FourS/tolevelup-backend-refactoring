package com.fours.tolevelup.repository.theme;

import com.fours.tolevelup.model.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query("select t from Theme t where t.id =:tid")
    Optional<Theme> findById(@Param("tid")int themeId);
}
