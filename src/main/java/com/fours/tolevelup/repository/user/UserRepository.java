package com.fours.tolevelup.repository.user;

import java.util.List;
import com.fours.tolevelup.model.UserDTO;
import com.fours.tolevelup.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.level = u.level + 1 where u.id = :uid")
    void levelUp(@Param("uid") String user_id);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.password =:nPassword where u =:user")
    void updatePassWord(@Param("user") User user, @Param("nPassword") String newPassword);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.name =:nName where u =:user")
    void updateName(@Param("user") User user, @Param("nName") String newName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.email =:nEmail where u =:user")
    void updateEmail(@Param("user") User user, @Param("nEmail") String newEmail);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.intro =:nIntro where u =:user")
    void updateIntro(@Param("user") User user, @Param("nIntro") String newIntro);

    @Query("select u.id,u.name from User u where u.id = :uid")
    List<UserDTO.feedUserData> getUserById(@Param("uid") String user_id);

}
