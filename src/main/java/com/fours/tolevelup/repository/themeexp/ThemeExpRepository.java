package com.fours.tolevelup.repository.themeexp;

import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.ThemeExp;
import com.fours.tolevelup.model.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ThemeExpRepository extends JpaRepository<ThemeExp, String>, ThemeExpCustomRepository {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ThemeExp t set t.exp_total = t.exp_total + :exp where t.user = :user and t.theme = :theme")
    void updateExp(@Param("exp") float mission_exp, @Param("user") User user, @Param("theme") Theme theme);

    @Modifying
    @Query("delete from ThemeExp t where t.user = :uid")
    void deleteAllByUser(@Param("uid") User user);

    @Query("select t from ThemeExp t where t.user.id=:uid")
    List<ThemeExp> getThemeExp(@Param("uid") String user_id);

    @Query("select te from ThemeExp te where te.user.id =:uid and te.theme.id =:tid")
    Optional<ThemeExp> getThemeExpByUserAndTheme(@Param("uid") String userId, @Param("tid") int themeId);

    @Query("select sum(t.exp_total) from ThemeExp t where t.user.id=:uid")
    int expTotal(@Param("uid") String user_id);

    @Query("select te.user from ThemeExp te group by te.user")
    Slice<User> findUserSortByUserId(Pageable pageable);

    @Query(value = "SELECT i.ranking FROM(SELECT RANK() OVER (ORDER BY SUM(te.exp_total) DESC) ranking, te.user_id " +
            "FROM theme_exp te GROUP BY te.user_id) i where i.user_id = :uid", nativeQuery = true)
    int rank(@Param("uid") String user_Id);

    @Query("select te.exp_total from ThemeExp te where te.user = :user and te.theme = :theme")
    int exp(@Param("user") User user, @Param("theme") Theme theme);

}
