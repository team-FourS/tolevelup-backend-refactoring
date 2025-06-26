package com.fours.tolevelup.repository;


import com.fours.tolevelup.model.entity.UserThemeRanking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserThemeRankingRepository extends JpaRepository<UserThemeRanking, Long> {

    @Query("SELECT r FROM UserThemeRanking r WHERE r.theme.id = :themeId "
            + "AND r.year = :year AND r.month = :month "
            + "AND r.ranking BETWEEN :start AND :end")
    Optional<List<UserThemeRanking>> findByRankingRange(int themeId, int year, int month, int start, int end);

    Optional<UserThemeRanking> findByUserIdAndThemeIdAndYearAndMonth(String userId, int themeId, int year, int month);

}


