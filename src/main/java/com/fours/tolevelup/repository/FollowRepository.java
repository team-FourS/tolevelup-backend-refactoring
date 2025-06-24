package com.fours.tolevelup.repository;

import com.fours.tolevelup.model.entity.Follow;
import com.fours.tolevelup.model.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Modifying
    @Query("delete from Follow f where f.followingUser =:user or f.fromUser =:user")
    void deleteAllByUser(@Param("user") User user);

    Optional<Follow> findByFromUserAndFollowingUser(User user, User follow);

    long countAllByFollowingUserId(String followingUserId);

    long countAllByFromUserId(String fromUserId);

    @Query("select f.followingUser from Follow f where f.fromUser.id = :uid")
    Slice<User> findByUser(@Param("uid") String userId, Pageable pageable);

    @Query("select f.fromUser from Follow f where f.followingUser.id = :uid")
    Slice<User> findByFollowingUser(@Param("uid") String userId, Pageable pageable);

}
