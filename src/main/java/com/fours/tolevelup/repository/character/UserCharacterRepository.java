package com.fours.tolevelup.repository.character;

import com.fours.tolevelup.model.entity.Character;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import com.fours.tolevelup.service.character.CharacterDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCharacterRepository extends JpaRepository<UserCharacter, String> {
    @Query("SELECT c FROM Character c WHERE c.level = 1")
    List<Character> findBylevel();

    @Query("SELECT uc FROM UserCharacter uc WHERE uc.user.id = :id")
    List<UserCharacter> getUserCharacter(@Param("id") String user_id);

    @Modifying
    @Query("delete from UserCharacter u where u.user = :uid")
    void deleteAllByUser(@Param("uid") User user);

    @Query ("select uc from UserCharacter uc where uc.user.id=:user_id and uc.character.id=:character_id")
    Optional<UserCharacter> findByUserIdandCharacterId(@Param("user_id") String user_id, @Param("character_id") String character_id);

    // themeId를 이용해서 userCharacater 객체 찾기
    @Query("select uc from UserCharacter uc where uc.id LIKE %:findCharacter%")
    UserCharacter findUserCharacterById(@Param("findCharacter") String findCharacter);

    @Query("select uc.character.level from UserCharacter uc where uc.id = :id")
    int getLevel(@Param("id") String id);

    @Query("select te.exp_total from ThemeExp te where te.user.id = :uid and te.theme.id = :tid")
    int getExp(@Param("uid") String user_id, @Param("tid") int theme_id);

    @Query("select c.theme.id from Character c where c.id = :cid")
    int getThemeId(@Param("cid") String character_id);

    @Query("select uc.character_name from UserCharacter uc where uc.user.id=:uid and uc.character.id=:cid")
    String getCharacterName(@Param("uid")String user_id, @Param("cid")String character_id);

    @Query("select uc.id from UserCharacter uc where uc.user.id=:uid and uc.character.id=:cid")
    String getUserCharacterId(@Param("uid")String user_id, @Param("cid")String character_id);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserCharacter uc set uc.character.id=:changeCharacter_id where uc.character.id = :character_id")
    void updateLevel(@Param("changeCharacter_id") String changeCharacter_id, @Param("character_id") String character_id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserCharacter uc set uc.character_name = :character_name where uc.user.id=:uid and uc.character.id=:cid")
    void updateName(@Param("character_name") String character_name, @Param("uid") String user_id, @Param("cid") String character_id);


    @Query("select uc from UserCharacter uc where uc.user = :user and uc.character.id like :name%")
    UserCharacter findUserCharacterByUserAndThemeName(@Param("user") User user, @Param("name") String theme_name);
    /*@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update UserCharacter uc set uc.character.id = left(uc.character.id, length(uc.character.id) - 1)  where uc.id=:id", nativeQuery = true)
    void cutLevel(@Param("id") String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserCharacter uc set uc.character.id = concat(uc.character.id, '2') where uc.id = :id")
    void updateCharacterLv1(@Param("id") String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserCharacter uc set uc.character.id = concat(uc.character.id, '3') where uc.id=:id")
    void updateCharacterLv2(@Param("id") String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserCharacter uc set uc.character.id = concat(uc.character.id, '4') where uc.id=:id")
    void updateCharacterLv3(@Param("id") String id);*/
}
