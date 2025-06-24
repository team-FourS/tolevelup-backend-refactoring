package com.fours.tolevelup.model.entity;

import com.fours.tolevelup.model.MissionStatus;
import com.fours.tolevelup.model.entity.Mission;
import com.fours.tolevelup.model.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@DynamicUpdate
@Entity
@AllArgsConstructor
@Table(name = "mission_log")
public class MissionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date start_date;

    private Timestamp update_at;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @PrePersist
    void registeredAt(){
        this.start_date = Date.valueOf(LocalDate.now());
    }

    @PreUpdate
    void updatedAt(){
        this.update_at = Timestamp.valueOf(LocalDateTime.now());
    }

    public void updateStatus(MissionStatus status) {
        this.status = status;
    }

    @Builder
    public MissionLog(User user, Mission mission, MissionStatus status) {
        this.user = user;
        this.mission = mission;
        this.status = status;
    }
}
