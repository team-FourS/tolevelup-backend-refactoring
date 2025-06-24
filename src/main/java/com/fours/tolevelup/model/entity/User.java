package com.fours.tolevelup.model.entity;


import com.fours.tolevelup.model.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void update(
            String password, String name, String email, String intro
    ) {
        if (password != null) this.password = password;
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (intro != null) this.intro = intro;
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
