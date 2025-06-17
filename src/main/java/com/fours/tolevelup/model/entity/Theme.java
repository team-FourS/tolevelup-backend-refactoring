package com.fours.tolevelup.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Theme {
    @Id
    private int id;
    private String name;
    private String type;
    //private SerialBlob img;

    @Builder
    public Theme(int id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

}

