package com.fours.tolevelup.service;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.AlarmType;
import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.Like;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.AlarmRepository;
import com.fours.tolevelup.repository.LikeRepository;
import com.fours.tolevelup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public boolean getUserLikeChecked(String fromId, String toId) {
        getUserOrException(fromId);
        getUserOrException(toId);
        return likeRepository.findByFromUserIdAndToUserId(fromId, toId).isPresent();
    }

    @Transactional
    public void createLike(String fromId, String toId) {
        User fromUser = getUserOrException(fromId);
        User toUser = getUserOrException(toId);
        likeRepository.findByFromUserIdAndToUserId(fromId, toId).ifPresent(it -> {
            throw new TluApplicationException(ErrorCode.ALREADY_LIKE);
        });
        likeRepository.save(Like.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build());
        alarmRepository.save(Alarm.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .alarmType(AlarmType.NEW_LIKE)
                .build()
        );
    }

    @Transactional
    public void deleteLike(String fromId, String toId) {
        getUserOrException(fromId);
        getUserOrException(toId);
        Like like = likeRepository.findByFromUserIdAndToUserId(fromId, toId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.LIKE_NOT_FOUND));
        likeRepository.delete(like);
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", id)));
    }

}
