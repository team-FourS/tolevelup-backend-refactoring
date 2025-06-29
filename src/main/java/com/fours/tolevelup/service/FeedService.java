package com.fours.tolevelup.service;


import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;
import com.fours.tolevelup.service.dto.FeedDTO;
import com.fours.tolevelup.service.dto.UserDTO;
import com.fours.tolevelup.service.dto.UserDTO.feedUserData;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import com.fours.tolevelup.repository.FollowRepository;
import com.fours.tolevelup.repository.LikeRepository;
import com.fours.tolevelup.repository.UserCharacterRepository;
import com.fours.tolevelup.repository.MissionLogRepository;
import com.fours.tolevelup.repository.UserRepository;
import com.fours.tolevelup.service.character.CharacterDTO.UserCharacterFeed;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FeedService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final MissionService missionService;
    private final LikeRepository likeRepository;
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
                            //.myLikeChecked(getUserLikeChecked(userId, feedUser.getId()))
                            .thisLikeCounts(getFeedLikeCount(feedUser.getId()))
                            //.thisCommentCounts(getFeedCommentCounts(feedUser.getId()))
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

    private boolean getFollowStatus(User fromUser, User followUser) {
        return followRepository.findByFromUserAndFollowingUser(fromUser, followUser).isPresent();
    }

    private long getFeedLikeCount(String userId) {
        User user = getUserOrException(userId);
        return likeRepository.countByToUser(user).orElseGet(() -> 0L);
    }

    public long getDateLikeCount(String userId, Date date) {
        User user = getUserOrException(userId);
        return likeRepository.countByDateAndToUser(date, user);
    }


    private User getUserOrException(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new TluApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", id)));
    }


}
