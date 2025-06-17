package com.fours.tolevelup.model.entity;

import com.fours.tolevelup.model.entity.Character;
import com.fours.tolevelup.model.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class UserCharacter {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character character;

    private String character_name;

    @PrePersist
    void persistSetting(){
        this.character_name = "character_name";
    }

    @Builder
    public UserCharacter(String id, User user, Character character, String character_name){
        this.id = id;
        this.user = user;
        this.character = character;
        this.character_name = character_name;
    }

    public void update(String character_name){
        this.character_name = character_name;
    }

}
