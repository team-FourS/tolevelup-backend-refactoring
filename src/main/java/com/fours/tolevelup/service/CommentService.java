package com.fours.tolevelup.service;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.FeedDTO;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.CommentRepository;
import com.fours.tolevelup.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public long receivedCommentsCounts(String userId) {
        return commentRepository.countAllReceivedByUser(userId).orElseGet(() -> 0L);
    }

    public Page<FeedDTO.CommentData> receivedComments(String userId, Pageable pageable) {
        User user = getUserOrException(userId);
        return commentRepository.findAllReceivedByUser(user, pageable).map(FeedDTO.CommentData::fromComment);
    }

    public long sentCommentsCounts(String userId) {
        return commentRepository.countAllSentByUser(userId).orElseGet(() -> 0L);
    }

    public Page<FeedDTO.CommentData> sentComments(String userId, Pageable pageable) {
        User user = getUserOrException(userId);
        return commentRepository.findAllSentByUser(user, pageable).map(FeedDTO.CommentData::fromComment);
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", id)));
    }

}
