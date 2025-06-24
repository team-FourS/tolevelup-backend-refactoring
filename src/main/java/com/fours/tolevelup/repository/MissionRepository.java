package com.fours.tolevelup.repository;

import com.fours.tolevelup.model.entity.Mission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {

    @Query("SELECT m.id FROM Mission m WHERE m.theme.id = :themeId")
    List<Integer> findMissionIdsByThemeId(int themeId);

    Mission findAllById(int id);

    Optional<Mission> findById(int id);
}
