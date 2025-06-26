package com.fours.tolevelup.repository;


import com.fours.tolevelup.model.entity.UserRanking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRankingRepository extends JpaRepository<UserRanking, Long> {


    Slice<UserRanking> findByYearAndMonth(
            int year, int month, Pageable pageable
    );
}











