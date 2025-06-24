package com.fours.tolevelup.model.entity;


import com.fours.tolevelup.model.AlarmType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "`alarm`")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(name = "registeredAt")
    private Timestamp registeredAt;

    @PrePersist
    void registeredAt(){
        this.registeredAt = java.sql.Timestamp.valueOf(LocalDateTime.now());
    }

    @Builder
    public Alarm(User fromUser, User toUser, AlarmType alarmType){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.alarmType = alarmType;
    }
}
