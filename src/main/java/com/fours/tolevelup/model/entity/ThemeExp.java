package com.fours.tolevelup.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ThemeExp {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name  = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    private float exp_total;

    @PrePersist
    void registeredAt(){
        this.exp_total = 0;
    }

    @Builder
    public ThemeExp(String id,User user, Theme theme, float exp_total){
        this.id = id;
        this.user = user;
        this.theme = theme;
        this.exp_total = exp_total;
    }
}
