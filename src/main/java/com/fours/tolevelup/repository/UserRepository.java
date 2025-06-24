package com.fours.tolevelup.repository;

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


}
