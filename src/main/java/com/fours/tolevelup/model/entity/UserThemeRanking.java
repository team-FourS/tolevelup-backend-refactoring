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
public class UserThemeRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;

    private int month;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    private Double totalExp;

    private Long ranking;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @Builder
    public UserThemeRanking(
            int year, int month, User user, Theme theme, Double totalExp, Long ranking
    ) {
        this.user = user;
        this.year = year;
        this.month = month;
        this.theme = theme;
        this.totalExp = totalExp;
        this.ranking = ranking;
    }

}

