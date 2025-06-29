package com.fours.tolevelup.repository;

import com.fours.tolevelup.model.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, String> {

    @Query("select c from Character c where c.level = 1")
    List<Character> findByLevel();

    @Query("select c from Character c where c.level = :level + 1 and c.theme.id = :tid")
    Character getLvUpCharacter(@Param("level") int level, @Param("tid") int theme_id);

    @Query("select c from Character c where c.level = :level - 1 and c.theme.id = :tid")
    Character getLvDownCharacter(@Param("level") int level, @Param("tid") int theme_id);
}