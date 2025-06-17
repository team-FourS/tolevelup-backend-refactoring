package com.fours.tolevelup.controller.api;

import com.fours.tolevelup.controller.request.CommentRequest;
import com.fours.tolevelup.controller.response.FeedResponse;
import com.fours.tolevelup.controller.response.Response;
import com.fours.tolevelup.model.FeedDTO;
import com.fours.tolevelup.service.FeedService;
import com.fours.tolevelup.service.character.CharacterDTO;
import com.fours.tolevelup.service.character.CharacterDTO.UserCharacterInfo;
import com.fours.tolevelup.service.character.CharacterService;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController {

    private final FeedService feedService;
    private final CharacterService characterService;

    @GetMapping
    public Response<List<FeedResponse.FeedData>> feedList(Authentication authentication, Pageable pageable) {
        return Response.success(
                feedService.getFeedList(authentication.getName(), pageable)
                        .stream().map(FeedResponse.FeedData::fromFeedDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/follow")
    public Response<List<FeedResponse.FeedData>> followFeedList(Authentication authentication, Pageable pageable) {
        return Response.success(
                feedService.getFollowingFeedList(authentication.getName(), pageable)
                        .stream().map(FeedResponse.FeedData::fromFeedDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/character/{id}")
    public Response<FeedResponse.CharacterData> characterData(Authentication authentication,
                                                             @PathVariable("id") String userId) {
        return Response.success(new FeedResponse.CharacterData(feedService.getCharacterData(userId)));
    }


    /*
        @GetMapping("/{userId}/likes")
        public Response<Long> likeCount(@PathVariable("userId")String userId){
            return Response.success(feedService.getFeedLikeCount(userId));
        }
    */
    @GetMapping("/{user_id}/likes/{date}")
    public Response<Long> likeCount(@PathVariable("user_id") String userId, @PathVariable("date") Date date) {
        return Response.success(feedService.getDateLikeCount(userId, date));
    }

    @PostMapping("/{id}/likes")
    public Response<String> likeCheck(Authentication authentication, @PathVariable("id") String userId) {
        feedService.checkLike(authentication.getName(), userId);
        return Response.success("좋아요 전송");
    }

    @DeleteMapping("/{id}/likes")
    public Response<String> likeCancel(Authentication authentication, @PathVariable("id") String userId) {
        feedService.deleteLike(authentication.getName(), userId);
        return Response.success("좋아요 취소");
    }

    @GetMapping("/{userId}/comments")
    public Response<Page<FeedResponse.FeedComments>> feedCommentList(@PathVariable("userId") String userId,
                                                                     Pageable pageable) {
        return Response.success(
                feedService.getFeedComments(userId, pageable).map(FeedResponse.FeedComments::fromComment));
    }

    @PostMapping("/{id}/comments")
    public Response<String> comment(Authentication authentication, @PathVariable("id") String userId,
                                    @RequestBody CommentRequest request) {
        feedService.sendComment(authentication.getName(), userId, request.getComment());
        return Response.success("코멘트 전송");
    }

    @PutMapping("/comments/{cid}")
    public Response<FeedResponse.Comment> modifyComment(Authentication authentication,
                                                        @PathVariable("cid") Long commentId,
                                                        @RequestBody CommentRequest request) {
        FeedDTO.CommentData modifyComment = feedService.modifyComment(authentication.getName(), commentId,
                request.getComment());
        return Response.success(FeedResponse.Comment.fromDTO(modifyComment));
    }

    @DeleteMapping("/comments/{cid}")
    public Response<String> deleteComment(Authentication authentication, @PathVariable("cid") Long commentId) {
        feedService.deleteComment(authentication.getName(), commentId);
        return Response.success("코멘트 삭제");
    }


}
