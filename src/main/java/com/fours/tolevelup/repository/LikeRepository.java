package com.fours.tolevelup.repository;

import com.fours.tolevelup.model.entity.Like;
import com.fours.tolevelup.model.entity.User;
import java.sql.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByFromUserIdAndToUserId(String fromUserId, String toUserId);

    long countAllByToUserId(String toUserId);

    @Query("select count(lk) from Like lk where lk.toUser = :user and lk.date >= current_date")
    Optional<Long> countByToUser(@Param("user") User user);

    @Query("select count(lk) from Like lk where lk.toUser =:user and lk.date =:date")
    long countByDateAndToUser(@Param("date") Date date, @Param("user") User user);

    @Modifying
    @Query("delete from Like l where l.fromUser =:user or l.toUser =:user")
    void deleteAllByUser(@Param("user") User user);
}
