package com.fours.tolevelup.repository;


import com.fours.tolevelup.model.entity.UserRanking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRankingRepository extends JpaRepository<UserRanking, Long> {

    @Query("SELECT r FROM UserRanking r WHERE r.year = :year AND r.month = :month "
            + "AND r.ranking BETWEEN :start AND :end")
    Optional<List<UserRanking>> findByRankingRange(int year, int month, int start, int end);

}
