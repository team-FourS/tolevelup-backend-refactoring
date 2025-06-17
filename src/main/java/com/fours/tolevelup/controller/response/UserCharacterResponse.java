package com.fours.tolevelup.controller.response;

import com.fours.tolevelup.service.character.CharacterDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCharacterResponse {

    @Getter
    @AllArgsConstructor
    public static class UserCharacter{
        private String id;
        private String user_id;
        private String character_id;
        private String character_name;

        public static UserCharacter fromDTO(CharacterDTO.UserCharacter userCharacter){
            return new UserCharacter(
                    userCharacter.getId(),
                    userCharacter.getUser_id(),
                    userCharacter.getCharacter_id(),
                    userCharacter.getCharacter_name()
            );
        }
    }
}
