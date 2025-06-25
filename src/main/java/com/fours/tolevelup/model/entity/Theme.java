package com.fours.tolevelup.model.entity;

import com.fours.tolevelup.model.ThemeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private ThemeType type;

    @Builder
    public Theme(int id, String name, ThemeType type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

}

