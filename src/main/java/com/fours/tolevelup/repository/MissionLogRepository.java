package com.fours.tolevelup.repository;

import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {

    @Query("select ml.user from MissionLog ml where ml.update_at >= current_date " +
            "group by ml.user order by ml.update_at desc")
    Slice<User> findUserSortByTodayEndTime(Pageable pageable);

    @Query("select distinct ml.user from MissionLog ml join fetch Follow f " +
            "on ml.user = f.followingUser " +
            "where ml.update_at >= current_date " +
            "and f.fromUser.id =:uid " +
            "group by ml.user order by ml.update_at desc")
    Slice<User> findFollowSortByTodayEndTime(@Param("uid") String userId, Pageable pageable);

    @Query("select ml from MissionLog ml where ml.user =:user and ml.mission.theme =:theme and ml.start_date >=:date")
    List<MissionLog> findUserTodayMissionByTheme(User user, Theme theme, Date date);

    Optional<MissionLog> findById(Long id);

    @Query("select ml from MissionLog ml where ml.user.id = :uid and ml.update_at >= current_date")
    List<MissionLog> findTodayCompleteByUser(@Param("uid") String userId);

    @Query("select count(ml) from MissionLog ml where ml.user =:user " +
            "and function('date_format',ml.update_at,'%Y-%m') <= current_date ")
    long countAllCompleteByUser(@Param("user") User user);

    @Query("select count(ml) from MissionLog ml where ml.user =:user and ml.mission.theme =:theme" +
            " and function('date_format',ml.update_at,'%Y-%m') <= current_date ")
    long countByTheme(@Param("user") User user, @Param("theme") Theme theme);

    @Query("select sum(ml.mission.exp) from MissionLog ml where ml.user =:user and " +
            "function('date_format',ml.update_at,'%Y-%m') =:date and ml.mission.theme =:theme")
    Optional<Long> expSumByDateAndTheme(@Param("user") User user, @Param("theme") Theme theme,
                                        @Param("date") String date);

    @Modifying
    @Query("delete from MissionLog m where m.user = :uid")
    void deleteAllByUser(@Param("uid") User user);

}
