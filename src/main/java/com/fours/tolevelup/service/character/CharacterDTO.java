package com.fours.tolevelup.service.character;

import com.fours.tolevelup.model.entity.Character;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.rowset.serial.SerialBlob;

@Setter
@Getter
@NoArgsConstructor
public class CharacterDTO {

    @Getter
    @Setter
    public static class Character{
        private String id;
        private int level;
        private String info;
        private SerialBlob img;
        private String theme_id;
    }


    @NoArgsConstructor
    @Getter
    @Setter
    public static class CharacterData{
        private String id;
        private int level;
        private String info;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class UserCharacter{
        private String id;
        private String user_id;
        private String character_id;
        private String character_name;

        public static UserCharacter fromUserCharacter(com.fours.tolevelup.model.entity.UserCharacter userCharacter){
            return new UserCharacter(
                    userCharacter.getId(),
                    userCharacter.getUser().getId(),
                    userCharacter.getCharacter().getId(),
                    userCharacter.getCharacter_name()
            );
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserCharacterFeed{
        private String character_id;
        private String character_name;

        public static UserCharacterFeed fromUserCharacter(com.fours.tolevelup.model.entity.UserCharacter userCharacterFeed){
            return new UserCharacterFeed(
                    userCharacterFeed.getCharacter().getId(),
                    userCharacterFeed.getCharacter_name()
            );
        }
    }



    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserCharacterInfo{
        private com.fours.tolevelup.model.entity.UserCharacter userCharacter;
        private float exp;
        private int level;
    }

    @Getter
    @Setter
    @Builder
    public static class CharacterNameUpdate{
        private String character_name;
    }
}
