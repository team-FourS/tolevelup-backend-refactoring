package com.fours.tolevelup.model.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Table(name = "`like`")
@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Column(name = "feed_date")
    private Date date;

    @PrePersist
    void registeredAt(){
        this.date = Date.valueOf(LocalDate.now());
    }

    @Builder
    public Like(User fromUser, User toUser){
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
