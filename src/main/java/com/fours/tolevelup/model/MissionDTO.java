package com.fours.tolevelup.model;

import com.fours.tolevelup.model.entity.Mission;
import com.fours.tolevelup.model.entity.MissionLog;
import com.fours.tolevelup.model.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


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
