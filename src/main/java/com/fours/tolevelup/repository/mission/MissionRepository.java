package com.fours.tolevelup.repository.mission;

import com.fours.tolevelup.model.entity.Mission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer>, MissionCustomRepository {

    @Query("SELECT m.id FROM Mission m WHERE m.theme.id = :themeId")
    List<Integer> findMissionIdsByThemeId(int themeId);

    Mission findAllById(int id);

    @Query("select m from Mission m where m.id = :mid")
    Optional<Mission> findById(@Param("mid") int id);
}
