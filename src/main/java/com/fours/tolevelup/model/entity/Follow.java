package com.fours.tolevelup.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User followingUser;

    @Column(name = "start_time")
    private Timestamp update_date;


    @PrePersist
    void registeredAt(){
        this.update_date = java.sql.Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    void updateAt(){
        this.update_date = java.sql.Timestamp.valueOf(LocalDateTime.now());
    }


    @Builder
    public Follow(User fromUser, User following_id){
        this.fromUser = fromUser;
        this.followingUser = following_id;
    }
}
