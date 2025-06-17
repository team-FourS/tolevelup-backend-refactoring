package com.fours.tolevelup.service;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.model.AlarmType;
import com.fours.tolevelup.model.FeedDTO;
import com.fours.tolevelup.model.UserDTO;
import com.fours.tolevelup.model.UserDTO.feedUserData;
import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.Comment;
import com.fours.tolevelup.model.entity.Like;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import com.fours.tolevelup.repository.AlarmRepository;
import com.fours.tolevelup.repository.CommentRepository;
import com.fours.tolevelup.repository.FollowRepository;
import com.fours.tolevelup.repository.LikeRepository;
import com.fours.tolevelup.repository.character.UserCharacterRepository;
import com.fours.tolevelup.repository.missionlog.MissionLogRepository;
import com.fours.tolevelup.repository.theme.ThemeRepository;
import com.fours.tolevelup.repository.user.UserRepository;
import com.fours.tolevelup.service.character.CharacterDTO.UserCharacterFeed;
import com.fours.tolevelup.service.mission.MissionServiceImpl;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FollowRepository followRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final MissionServiceImpl missionService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final MissionLogRepository missionLogRepository;
    private final UserCharacterRepository userCharacterRepository;

    public List<FeedDTO.feedData> getFeedList(String userId, Pageable pageable) {
        Slice<User> users = missionLogRepository.findUserSortByTodayEndTime(pageable);
        return getFeedData(userId, users);
    }

    public List<FeedDTO.feedData> getFollowingFeedList(String userId, Pageable pageable) {
        Slice<User> followingUsers = missionLogRepository.findFollowSortByTodayEndTime(userId, pageable);
        return getFeedData(userId, followingUsers);
    }

    private List<FeedDTO.feedData> getFeedData(String userId, Slice<User> userList) {
        User user = getUserOrException(userId);
        List<FeedDTO.feedData> feedDataList = new ArrayList<>();
        for (User feedUser : userList) {
            feedDataList.add(
                    FeedDTO.feedData.builder()
                            .userData(UserDTO.publicUserData.fromUser(feedUser))
                            .followStatus(getFollowStatus(user, feedUser))
                            .userCompleteMissions(missionService.userToDayCompleteList(feedUser.getId()))
                            .myLikeChecked(getUserLikeChecked(userId, feedUser.getId()))
                            .thisLikeCounts(getFeedLikeCount(feedUser.getId()))
                            .thisCommentCounts(getFeedCommentCounts(feedUser.getId()))
                            .build()
            );
        }
        return feedDataList;
    }

    public List<FeedDTO.CharacterData> getCharacterData(String userId){
        User user = getUserOrException(userId);
        List<UserCharacter> userCharacterList = userCharacterRepository.getUserCharacter(userId);
        List<FeedDTO.CharacterData> characterDataList = new ArrayList<>();

        for(UserCharacter userCharacter : userCharacterList){
            characterDataList.add(FeedDTO.CharacterData.builder()
                    .feedUserData(feedUserData.fromUser(user))
                    .userCharacterFeed(UserCharacterFeed.fromUserCharacter(userCharacter))
                    .level(userCharacter.getCharacter().getLevel())
                    .count(missionLogRepository.countByTheme(user, userCharacter.getCharacter().getTheme()))
                    .build());
        }

        return characterDataList;

    }

    private boolean getUserLikeChecked(String fromId, String toId) {
        User fromUser = getUserOrException(fromId);
        User toUser = getUserOrException(toId);
        return likeRepository.findByUserAndFeedUser(fromUser, toUser).isPresent();
    }

    private boolean getFollowStatus(User fromUser, User followUser) {
        return followRepository.findByFromUserAndFollowingUser(fromUser, followUser).isPresent();
    }


    @Transactional
    public void checkLike(String fromId, String toId) {
        User fromUser = getUserOrException(fromId);
        User toUser = getUserOrException(toId);
        likeRepository.findByUserAndFeedUser(fromUser, toUser).ifPresent(it -> {
            throw new TluApplicationException(ErrorCode.ALREADY_LIKE);
        });
        likeRepository.save(Like.builder().fromUser(fromUser).toUser(toUser).build());
        alarmRepository.save(Alarm.builder().toUser(toUser).fromUser(fromUser).alarmType(AlarmType.NEW_LIKE).build());
    }

    @Transactional
    public void deleteLike(String fromId, String toId) {
        User fromUser = getUserOrException(fromId);
        User toUser = getUserOrException(toId);
        Like like = likeRepository.findByUserAndFeedUser(fromUser, toUser).orElseThrow(() ->
                new TluApplicationException(ErrorCode.LIKE_NOT_FOUND));
        likeRepository.delete(like);
    }

    private long getFeedLikeCount(String userId) {
        User user = getUserOrException(userId);
        return likeRepository.countByToUser(user).orElseGet(() -> 0L);
    }

    public long getDateLikeCount(String userId, Date date) {
        User user = getUserOrException(userId);
        return likeRepository.countByDateAndToUser(date, user);
    }

    private long getFeedCommentCounts(String userId) {
        return commentRepository.findByFeedUser(userId).orElseGet(() -> 0L);
    }

    public Page<FeedDTO.CommentData> getFeedComments(String userId, Pageable pageable) {
        User feedUser = getUserOrException(userId);
        Page<FeedDTO.CommentData> comments = commentRepository.findByUser(feedUser, pageable)
                .map(FeedDTO.CommentData::fromComment);
        return comments;
    }

    @Transactional
    public void sendComment(String fromId, String toId, String comment) {
        User fromUser = getUserOrException(fromId);
        User toUser = getUserOrException(toId);
        commentRepository.save(Comment.builder().fromUser(fromUser).toUser(toUser).comment(comment).build());
        alarmRepository.save(
                Alarm.builder().toUser(toUser).fromUser(fromUser).alarmType(AlarmType.NEW_COMMENT).build());
    }

    @Transactional
    public FeedDTO.CommentData modifyComment(String userId, Long commentId, String modifyComment) {
        User user = getUserOrException(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.COMMENT_NOT_FOUND));
        userSameCheck(user, comment.getFromUser());
        commentRepository.updateComment(commentId, modifyComment, java.sql.Timestamp.valueOf(LocalDateTime.now()));
        alarmRepository.save(
                Alarm.builder().toUser(comment.getToUser()).fromUser(user).alarmType(AlarmType.MODIFY_COMMENT).build());
        return FeedDTO.CommentData.fromComment(commentRepository.findById(commentId).get());
    }

    @Transactional
    public void deleteComment(String userId, Long commentId) {
        User user = getUserOrException(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new TluApplicationException(ErrorCode.COMMENT_NOT_FOUND));
        userSameCheck(user, comment.getFromUser());
        commentRepository.delete(comment);
    }

    private void userSameCheck(User user, User checkUser) {
        if (user != checkUser) {
            throw new TluApplicationException(ErrorCode.INVALID_PERMISSION);
        }
    }

    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", id)));
    }


}
