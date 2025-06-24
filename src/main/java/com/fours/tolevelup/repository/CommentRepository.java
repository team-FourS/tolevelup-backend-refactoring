package com.fours.tolevelup.repository;


import com.fours.tolevelup.model.entity.Comment;
import com.fours.tolevelup.model.entity.User;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    @Query("select c from Comment c where c.toUser =:user and c.registeredAt >= current_date")
    Page<Comment> findAllTodayCommentsByUser(User user, Pageable pageable);

    long countAllByToUserId(String userId);

    long countAllByFromUserId(String userId);

    Page<Comment> findAllByToUserId(String userId, Pageable pageable);

    Page<Comment> findAllByFromUserId(String userId, Pageable pageable);

    @Modifying
    @Query("delete from Comment c where c.fromUser =:user or c.toUser =:user")
    void deleteAllByUser(@Param("user") User user);
}
