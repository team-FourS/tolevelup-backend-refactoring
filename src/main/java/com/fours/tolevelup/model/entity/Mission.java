package com.fours.tolevelup.model.entity;

import com.fours.tolevelup.model.entity.Theme;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Mission {
    @Id
    private int id;
    private String content;
    private float exp;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Builder
    public Mission(int id, float exp, String content, Theme theme){
        this.id = id;
        this.content = content;
        this.exp = exp;
        this.theme = theme;
    }

}

