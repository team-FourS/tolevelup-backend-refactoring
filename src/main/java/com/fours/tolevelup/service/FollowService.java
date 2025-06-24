package com.fours.tolevelup.service;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.AlarmType;
import com.fours.tolevelup.model.UserDTO;
import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.Follow;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.repository.AlarmRepository;
import com.fours.tolevelup.repository.FollowRepository;
import com.fours.tolevelup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(String userId, String followingId) {
        User user = getUserOrException(userId);
        User followingUser = getUserOrException(followingId);
        followRepository.findByFromUserAndFollowingUser(user, followingUser).ifPresent(it -> {
                    throw new TluApplicationException(ErrorCode.ALREADY_FOLLOW);
                }
        );
        Follow follow = Follow.builder()
                .fromUser(user)
                .following_id(followingUser)
                .build();
        followRepository.save(follow);
        alarmRepository.save(Alarm.builder().toUser(followingUser).fromUser(user).alarmType(AlarmType.FOLLOW).build());
    }

    @Transactional
    public void unfollow(String userId, String followingId) {
        User user = getUserOrException(userId);
        User followingUser = getUserOrException(followingId);
        Follow follow = followRepository.findByFromUserAndFollowingUser(user, followingUser).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", followingId)));
        followRepository.delete(follow);
    }

    @Transactional
    public void unfollowFromUser(String userId, String fromId) {
        User toUser = getUserOrException(userId);
        User fromUser = getUserOrException(fromId);
        Follow follow = followRepository.findByFromUserAndFollowingUser(fromUser, toUser).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", fromId)));
        followRepository.delete(follow);
    }

    public long getFollowingCounts(String userId) {
        User user = getUserOrException(userId);
        return followRepository.countAllByFromUserId(userId);
    }

    public Slice<UserDTO.publicUserData> getFollowingList(String userId, Pageable pageable) {
        return followRepository
                .findAllByFromUserId(userId, pageable)
                .map(follow -> UserDTO.publicUserData.fromUser(follow.getFollowingUser()));
    }

    public Slice<UserDTO.publicUserData> getFollowerList(String userId, Pageable pageable) {
        return followRepository
                .findAllByFollowingUserId(userId, pageable)
                .map(follow -> UserDTO.publicUserData.fromUser(follow.getFromUser()));

    }

    public long getFollowerCounts(String userId) {
        getUserOrException(userId);
        return followRepository.countAllByFollowingUserId(userId);
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", id)));
    }

}
