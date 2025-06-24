package com.fours.tolevelup.controller.response;


import com.fours.tolevelup.service.dto.FeedDTO;
import com.fours.tolevelup.service.dto.MissionDTO;
import com.fours.tolevelup.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
public class FeedResponse {

    @Getter
    @AllArgsConstructor
    public static class FeedData{
        private UserDTO.publicUserData userData;
        private boolean followStatus;
        private List<MissionDTO.mission> userCompleteMissions;
        private boolean likeSent;
        private long thisLikeCounts;
        private long thisCommentCounts;
        public static FeedData fromFeedDto(FeedDTO.feedData feedData){
            return new FeedData(
                    feedData.getUserData(),
                    feedData.isFollowStatus(),
                    feedData.getUserCompleteMissions(),
                    feedData.isMyLikeChecked(),
                    feedData.getThisLikeCounts(),
                    feedData.getThisCommentCounts()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CharacterData{
        private List<FeedDTO.CharacterData> characterDataList;
    }

    @Getter
    @AllArgsConstructor
    public static class FeedComments{
        private Long commentId;
        private UserDTO.publicUserData fromUserData;
        private String comment;
        private Timestamp registeredAt;
        private Timestamp updatedAt;

        public static FeedComments fromComment(FeedDTO.CommentData commentData){
            return new FeedComments(
                    commentData.getCommentId(),
                    commentData.getFromUserData(),
                    commentData.getComment(),
                    commentData.getRegisteredAt(),
                    commentData.getUpdatedAt()
            );
        }
    }

}
