package com.fours.tolevelup.model;


import com.fours.tolevelup.model.FeedDTO.CharacterData;
import com.fours.tolevelup.model.entity.Comment;
import com.fours.tolevelup.service.character.CharacterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
public class FeedDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class feedData{
        private UserDTO.publicUserData userData;
        private boolean followStatus;
        private List<MissionDTO.mission> userCompleteMissions;
        private boolean myLikeChecked;
        private long thisLikeCounts;
        private long thisCommentCounts;
    }


    @Getter
    @AllArgsConstructor
    public static class CommentData{
        private Long commentId;
        private UserDTO.publicUserData fromUserData;
        private UserDTO.publicUserData toUserData;
        private String comment;
        private Timestamp registeredAt;
        private Timestamp updatedAt;

        public static CommentData fromComment(Comment comment){
            return new CommentData(
                    comment.getId(),
                    UserDTO.publicUserData.fromUser(comment.getFromUser()),
                    UserDTO.publicUserData.fromUser(comment.getToUser()),
                    comment.getComment(),
                    comment.getRegisteredAt(),
                    comment.getUpdatedAt()
            );
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CharacterData{

        private UserDTO.feedUserData feedUserData;
        private CharacterDTO.UserCharacterFeed userCharacterFeed;
        private int level;
        private long count;

    }
}
