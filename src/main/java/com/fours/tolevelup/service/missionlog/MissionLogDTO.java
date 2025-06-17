package com.fours.tolevelup.service.missionlog;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public class MissionLogDTO {

    private int id;
    private String user_id;
    private int mission_id;
    private Timestamp start_date;
    private Timestamp end_date;
    private String status;

}
