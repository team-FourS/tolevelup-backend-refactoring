package com.fours.tolevelup.controller.response;


import com.fours.tolevelup.model.FeedDTO;
import com.fours.tolevelup.model.MissionDTO;
import com.fours.tolevelup.model.UserDTO;
import com.fours.tolevelup.model.entity.Comment;
import com.fours.tolevelup.service.character.CharacterDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.sql.Date;
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

    @Getter
    @AllArgsConstructor
    public static class Comment{
        private Long commentId;
        private UserDTO.publicUserData fromUserData;
        private UserDTO.publicUserData toUserData;
        private String comment;
        private Timestamp registeredAt;
        private Timestamp updatedAt;

        public static Comment fromDTO(FeedDTO.CommentData commentData){
            return new Comment(
                    commentData.getCommentId(),
                    commentData.getFromUserData(),
                    commentData.getToUserData(),
                    commentData.getComment(),
                    commentData.getRegisteredAt(),
                    commentData.getUpdatedAt()
            );
        }
    }

}
