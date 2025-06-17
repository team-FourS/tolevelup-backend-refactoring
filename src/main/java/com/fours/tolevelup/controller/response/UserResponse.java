package com.fours.tolevelup.controller.response;


import com.fours.tolevelup.model.*;
import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.Comment;
import com.fours.tolevelup.model.entity.Theme;
import com.fours.tolevelup.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import javax.inject.Inject;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserResponse {

    @Getter
    @AllArgsConstructor
    public static class LoginData{
        private String token;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserAllData{
        private UserData userData;
        private List<UserExpData> expData;
    }

    @Getter
    @AllArgsConstructor
    public static class UserData{
        private String id;
        private String name;
        private String email;
        private int level;
        private String intro;
        private UserRole role;
        private Date registeredAt;
        public static UserData fromUserDTO(UserDTO user){
            return new UserData(
                    user.getId(), user.getName(),
                    user.getEmail(), user.getLevel(),
                    user.getIntro(), user.getRole(),
                    user.getRegisteredAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class UserExpData{
        private Theme themeData;
        private float expData;
        public static UserExpData fromExpDTO(ThemeExpDTO themeExp){
            return new UserExpData(
                    themeExp.getTheme(),
                    themeExp.getExp_total()
            );
        }
    }


    @Getter
    @AllArgsConstructor
    public static class UserPublicData{
        private String userId;
        private String name;
        private int level;
        private String intro;

        public static UserPublicData fromDTO(UserDTO.publicUserData userData) {
            return new UserPublicData(
                    userData.getUserId(),
                    userData.getName(),
                    userData.getLevel(),
                    userData.getIntro()
            );
        }
    }






    @Getter
    @AllArgsConstructor
    public static class FollowerList{
        private Slice<UserDTO.publicUserData> followerData;
    }

    @Getter
    @AllArgsConstructor
    public static class SentComments{
        private Long commentId;
        private UserDTO.publicUserData toUserData;
        private String comment;
        private Timestamp registeredAt;
        private Timestamp updatedAt;
        public static SentComments fromComment(FeedDTO.CommentData comment){
            return new SentComments(
                    comment.getCommentId(),
                    comment.getToUserData(),
                    comment.getComment(),
                    comment.getRegisteredAt(),
                    comment.getUpdatedAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ReceivedComments{
        private Long commentId;
        private UserDTO.publicUserData fromUserData;
        private String comment;
        private Timestamp registeredAt;
        private Timestamp updatedAt;
        public static ReceivedComments fromComment(FeedDTO.CommentData comment){
            return new ReceivedComments(
                    comment.getCommentId(),
                    comment.getFromUserData(),
                    comment.getComment(),
                    comment.getRegisteredAt(),
                    comment.getUpdatedAt()
            );
        }
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserAlarmList{
        private Long alarmId;
        private String fromUserId;
        private AlarmType alarmType;
        private Timestamp registeredAt;

        public static UserAlarmList fromDTO(AlarmDTO alarm){
            return new UserAlarmList(
                    alarm.getAlarmId(),
                    alarm.getFromUserId(),
                    alarm.getAlarmType(),
                    alarm.getRegisteredAt()
            );
        }
    }

}
