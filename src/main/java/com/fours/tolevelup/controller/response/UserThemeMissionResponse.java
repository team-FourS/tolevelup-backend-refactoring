package com.fours.tolevelup.controller.response;


import com.fours.tolevelup.model.MissionStatus;
import com.fours.tolevelup.model.entity.MissionLog;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserThemeMissionResponse {
    private Long missionLogId;
    private String content;
    private MissionStatus status;
    private float exp;

    public static UserThemeMissionResponse fromMissionLog(MissionLog missionLog) {
        return new UserThemeMissionResponse(
                missionLog.getId(),
                missionLog.getMission().getContent(),
                missionLog.getStatus(),
                missionLog.getMission().getExp()
        );
    }
}
