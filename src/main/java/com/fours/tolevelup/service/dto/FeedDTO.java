package com.fours.tolevelup.service.dto;


import com.fours.tolevelup.service.character.CharacterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    @Builder
    @AllArgsConstructor
    public static class CharacterData{

        private UserDTO.feedUserData feedUserData;
        private CharacterDTO.UserCharacterFeed userCharacterFeed;
        private int level;
        private long count;

    }
}
