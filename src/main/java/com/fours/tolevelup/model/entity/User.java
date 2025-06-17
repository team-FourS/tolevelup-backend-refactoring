package com.fours.tolevelup.model.entity;


import com.fours.tolevelup.model.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GenericGenerator(name = "id",strategy = "uuid")
    private String id;
    private String password;
    private String name;
    private String email;
    private int level;
    private String intro;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "register_at")
    private Date registeredAt;

    @Transient
    private int rank;

    @PrePersist
    void registeredAt(){
        this.level = 1;
        this.intro = "자신을 한줄로 소개해주세요.";
        this.registeredAt = Date.valueOf(LocalDate.now());
    }

    @Builder
    public User(String id,String password,String name,String email,int level,String intro){
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.level = level;
        this.intro = intro;
    }

}
