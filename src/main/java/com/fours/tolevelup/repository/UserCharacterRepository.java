package com.fours.tolevelup.repository;

import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserCharacterRepository extends JpaRepository<UserCharacter, String> {

    @Query("SELECT uc FROM UserCharacter uc WHERE uc.user.id = :id")
    List<UserCharacter> getUserCharacter(@Param("id") String user_id);
    List<UserCharacter> findUserCharacterByUserId(String userId);

    @Modifying
    @Query("delete from UserCharacter u where u.user = :uid")
    void deleteAllByUser(@Param("uid") User user);

    UserCharacter findByIdAndUserId(String id, String userId);
    @Query("select uc.character.level from UserCharacter uc where uc.id = :id")
    int getLevel(@Param("id") String id);

    @Query("select te.exp_total from ThemeExp te where te.user.id = :uid and te.theme.id = :tid")
    int getExp(@Param("uid") String user_id, @Param("tid") int theme_id);

    @Query("select c.theme.id from Character c where c.id = :cid")
    int getThemeId(@Param("cid") String character_id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserCharacter uc set uc.character.id=:changeCharacter_id where uc.character.id = :character_id")
    void updateLevel(@Param("changeCharacter_id") String changeCharacter_id, @Param("character_id") String character_id);

    @Query("select uc from UserCharacter uc where uc.user = :user and uc.character.id like :name%")
    UserCharacter findUserCharacterByUserAndThemeName(@Param("user") User user, @Param("name") String theme_name);
}
