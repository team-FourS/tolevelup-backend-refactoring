package com.fours.tolevelup.repository;


import com.fours.tolevelup.model.entity.UserThemeRanking;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserThemeRankingRepository extends JpaRepository<UserThemeRanking, Long> {

    Page<UserThemeRanking> findByThemeIdAndYearAndMonthAndRankingBetween(
            int themeId, int year, int month, Long ranking, Long ranking2, Pageable pageable
    );

    Slice<UserThemeRanking> findByThemeIdAndYearAndMonth(
            int themeId, int year, int month, Pageable pageable
    );

    Optional<UserThemeRanking> findByUserIdAndThemeIdAndYearAndMonth(
            String userId, int themeId, int year, int month
    );

}


