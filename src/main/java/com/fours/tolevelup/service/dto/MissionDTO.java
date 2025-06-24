package com.fours.tolevelup.service.dto;

import com.fours.tolevelup.model.MissionStatus;
import com.fours.tolevelup.model.entity.MissionLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MissionDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class mission{
        private String themeName;
        private int missionId;
        private String content;
        private MissionStatus checked;
        private float exp;

        public static MissionDTO.mission fromMissionLog(MissionLog missionLog){
            return new MissionDTO.mission(
                    missionLog.getMission().getTheme().getName(),
                    missionLog.getMission().getId(),
                    missionLog.getMission().getContent(),
                    missionLog.getStatus(),
                    missionLog.getMission().getExp()
            );
        }
    }



}
