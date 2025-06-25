package com.fours.tolevelup.service;


import com.fours.tolevelup.controller.response.CommentResponse;
import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.AlarmType;
import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.Comment;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.AlarmRepository;
import com.fours.tolevelup.repository.CommentRepository;
import com.fours.tolevelup.repository.UserRepository;
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
    public void createComment(String fromId, String toId, String comment) {
        User fromUser = getUserOrException(fromId);
        User toUser = getUserOrException(toId);
        commentRepository.save(Comment
                .builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .comment(comment)
                .build());
        alarmRepository.save(Alarm
                .builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .alarmType(AlarmType.NEW_COMMENT)
                .build());
    }

    @Transactional
    public CommentResponse modifyComment(String userId, Long commentId, String modifyComment) {
        getUserOrException(userId);
        Comment comment = getCommentOrException(commentId);
        userRightCheck(userId, comment.getFromUser());
        comment.updateComment(modifyComment);
        return CommentResponse.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(String userId, Long commentId) {
        getUserOrException(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.COMMENT_NOT_FOUND));
        userRightCheck(userId, comment.getFromUser());
        commentRepository.delete(comment);
    }

    public Page<CommentResponse> getTodayComments(String userId, Pageable pageable) {
        User feedUser = getUserOrException(userId);
        return commentRepository
                .findAllTodayCommentsByUser(feedUser, pageable)
                .map(CommentResponse::fromEntity);
    }

    public Page<CommentResponse> findAllReceivedComments(String userId, Pageable pageable) {
        User user = getUserOrException(userId);
        return commentRepository.
                findAllByToUserId(user.getId(), pageable)
                .map(CommentResponse::fromEntity);
    }

    public Page<CommentResponse> findAllSentComments(String userId, Pageable pageable) {
        User user = getUserOrException(userId);
        return commentRepository
                .findAllByFromUserId(user.getId(), pageable)
                .map(CommentResponse::fromEntity);
    }

    public long receivedCommentsCounts(String userId) {
        return commentRepository.countAllByToUserId(userId);
    }

    public long sentCommentsCounts(String userId) {
        return commentRepository.countAllByFromUserId(userId);
    }

    private void userRightCheck(String user, User checkUser) {
        if (!user.equals(checkUser.getId())) {
            throw new TluApplicationException(ErrorCode.INVALID_PERMISSION);
        }
    }

    private Comment getCommentOrException(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", id)));
    }

}
