package com.fours.tolevelup.repository;

import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select a from Alarm a where a.toUser.id = :toUser order by a.registeredAt desc")
    Slice<Alarm> findAllByToUser(@Param("toUser") String toUser, Pageable pageable);

    @Modifying
    @Query("delete from Alarm a where a.toUser =:user")
    void deleteByUser(@Param("user") User user);

    @Modifying
    @Query("delete from Alarm a where a.toUser =:user or a.fromUser =:user")
    void deleteAllByUser(@Param("user") User user);
}
