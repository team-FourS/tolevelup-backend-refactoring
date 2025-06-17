package com.fours.tolevelup.controller.response;


import com.fours.tolevelup.model.MissionDTO;
import com.fours.tolevelup.model.entity.Mission;
import com.fours.tolevelup.model.entity.MissionLog;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class MissionResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class all{
        public List<MissionDTO.mission> dailyMissions;
        public List<MissionDTO.mission> weeklyMissions;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class type{
        public List<MissionDTO.mission> missions;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class ThemeMissions{
        private int missionId;
        private String content;
        private String checked;
        private float exp;
        public static ThemeMissions fromMissionDTO(MissionDTO.mission missionDTO){
            return new ThemeMissions(
                    missionDTO.getMissionId(),
                    missionDTO.getContent(),
                    missionDTO.getChecked().toString(),
                    missionDTO.getExp()
            );
        }
    }

}
