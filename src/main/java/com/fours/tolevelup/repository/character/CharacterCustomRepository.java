package com.fours.tolevelup.repository.character;

import com.fours.tolevelup.model.entity.Character;

import java.util.List;
public interface CharacterCustomRepository {
    Character findById(String id);
/*    List<Character> findByLevel();*/
}
