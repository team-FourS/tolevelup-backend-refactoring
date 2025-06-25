package com.fours.tolevelup.repository.character;

import com.fours.tolevelup.model.entity.Character;

public interface CharacterCustomRepository {
    Character findById(String id);
}
