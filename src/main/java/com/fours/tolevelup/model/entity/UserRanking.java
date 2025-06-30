package com.fours.tolevelup.model.entity;


import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;

    private int month;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Double totalExp;

    private Long ranking;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @Builder
    public UserRanking(int year, int month, User user, Double totalExp, Long ranking) {
        this.year = year;
        this.month = month;
        this.user = user;
        this.totalExp = totalExp;
        this.ranking = ranking;
    }

}






