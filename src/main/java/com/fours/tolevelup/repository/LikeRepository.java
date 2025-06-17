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

    @Query("select lk from Like lk where lk.fromUser = :f_user and lk.date = current_date and lk.toUser = :t_user")
    Optional<Like> findByUserAndFeedUser(@Param("f_user") User fromUser, @Param("t_user") User toUser);

    @Query("select count(lk) from Like lk where lk.toUser = :user")
    long countAllByToUser(@Param("user") User user);

    @Query("select count(lk) from Like lk where lk.toUser = :user and lk.date >= current_date")
    Optional<Long> countByToUser(@Param("user") User user);

    @Query("select count(lk) from Like lk where lk.toUser =:user and lk.date =:date")
    long countByDateAndToUser(@Param("date") Date date, @Param("user") User user);

    @Modifying
    @Query("delete from Like l where l.fromUser =:user or l.toUser =:user")
    void deleteAllByUser(@Param("user") User user);
}
