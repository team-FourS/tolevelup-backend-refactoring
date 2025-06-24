package com.fours.tolevelup.service;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.AlarmType;
import com.fours.tolevelup.model.FeedDTO;
import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.Comment;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.AlarmRepository;
import com.fours.tolevelup.repository.CommentRepository;
import com.fours.tolevelup.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;


    @Transactional
    public FeedDTO.CommentData modifyComment(String userId, Long commentId, String modifyComment) {
        User user = getUserOrException(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.COMMENT_NOT_FOUND));
        userSameCheck(user, comment.getFromUser());
        comment.updateComment(modifyComment);
        return FeedDTO.CommentData.fromComment(comment);
    }

    @Transactional
    public void deleteComment(String userId, Long commentId) {
        User user = getUserOrException(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.COMMENT_NOT_FOUND));
        userSameCheck(user, comment.getFromUser());
        commentRepository.delete(comment);
    }


    public Page<FeedDTO.CommentData> getFeedComments(String userId, Pageable pageable) {
        User feedUser = getUserOrException(userId);
        return commentRepository.findAllTodayCommentsByUser(feedUser, pageable)
                .map(FeedDTO.CommentData::fromComment);
    }

    @Transactional
    public void sendComment(String fromId, String toId, String comment) {
        User fromUser = getUserOrException(fromId);
        User toUser = getUserOrException(toId);
        commentRepository.save(Comment.builder().fromUser(fromUser).toUser(toUser).comment(comment).build());
        alarmRepository.save(
                Alarm.builder().toUser(toUser).fromUser(fromUser).alarmType(AlarmType.NEW_COMMENT).build());
    }

    public long receivedCommentsCounts(String userId) {
        return commentRepository.countAllByToUserId(userId);
    }

    public Page<FeedDTO.CommentData> receivedComments(String userId, Pageable pageable) {
        User user = getUserOrException(userId);
        return commentRepository.findAllByToUserId(user.getId(), pageable).map(FeedDTO.CommentData::fromComment);
    }

    public long sentCommentsCounts(String userId) {
        return commentRepository.countAllByFromUserId(userId);
    }

    public Page<FeedDTO.CommentData> sentComments(String userId, Pageable pageable) {
        User user = getUserOrException(userId);
        return commentRepository.findAllByFromUserId(user.getId(), pageable).map(FeedDTO.CommentData::fromComment);
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", id)));
    }

    private void userSameCheck(User user, User checkUser) {
        if (user != checkUser) {
            throw new TluApplicationException(ErrorCode.INVALID_PERMISSION);
        }
    }

}
