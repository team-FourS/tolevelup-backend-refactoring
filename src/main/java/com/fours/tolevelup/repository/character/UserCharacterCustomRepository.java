package com.fours.tolevelup.repository.character;

import com.fours.tolevelup.model.entity.UserCharacter;

import java.util.List;
public interface UserCharacterCustomRepository {

    List<UserCharacter> findByUserId(String user_id);

    void saveUserCharacter(UserCharacter userCharacter);



/*    List<UserCharacter> findByUser_IdAndCharacter_Id(String user_id, String character_id);*/
}
