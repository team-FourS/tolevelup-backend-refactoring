package com.fours.tolevelup.controller.response;


import com.fours.tolevelup.model.UserDTO;
import com.fours.tolevelup.model.UserDTO.publicUserData;
import com.fours.tolevelup.model.entity.Comment;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private UserDTO.publicUserData fromUserData;
    private UserDTO.publicUserData toUserData;
    private String comment;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static CommentResponse fromEntity(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                publicUserData.fromUser(comment.getFromUser()),
                publicUserData.fromUser(comment.getToUser()),
                comment.getComment(),
                comment.getRegisteredAt(),
                comment.getUpdatedAt()
        );
    }

}
