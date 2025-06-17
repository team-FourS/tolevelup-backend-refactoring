package com.fours.tolevelup.repository;


import com.fours.tolevelup.model.entity.Comment;
import com.fours.tolevelup.model.entity.User;
import java.sql.Timestamp;
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

    @Query("select c from Comment c where c.toUser.id =:to_uid and c.fromUser.id =:f_uid " +
            "and c.registeredAt >= current_date ")
    Optional<Comment> findFirstByByUserAndFeedUser(@Param("f_uid") String fromUser, @Param("to_uid") String feedUser);

    @Query("select count(c) from Comment c where c.toUser.id =:uid and c.registeredAt >= current_date ")
    Optional<Long> findByFeedUser(@Param("uid") String feedUserId);

    @Query("select c from Comment c where c.toUser =:user and c.registeredAt >= current_date order by c.registeredAt desc")
    Page<Comment> findByUser(@Param("user") User user, Pageable pageable);

    @Query("select count(c) from Comment c where c.toUser.id =:uid")
    Optional<Long> countAllReceivedByUser(@Param("uid") String userId);

    @Query("select c from Comment c where c.toUser =:user order by c.registeredAt desc")
    Page<Comment> findAllReceivedByUser(@Param("user") User user, Pageable pageable);

    @Query("select count(c) from Comment c where c.fromUser.id =:uid")
    Optional<Long> countAllSentByUser(@Param("uid") String userId);

    @Query("select c from Comment c where c.fromUser =:user order by c.registeredAt desc")
    Page<Comment> findAllSentByUser(@Param("user") User user, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Comment cm set cm.comment =:comment, cm.updatedAt =:time where cm.id =:cid")
    void updateComment(@Param("cid") Long commentId, @Param("comment") String comment,
                       @Param("time") Timestamp updateTime);

    @Modifying
    @Query("delete from Comment c where c.fromUser =:user or c.toUser =:user")
    void deleteAllByUser(@Param("user") User user);
}
