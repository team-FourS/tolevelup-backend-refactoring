package com.fours.tolevelup.model;

import com.fours.tolevelup.model.entity.Alarm;
import com.fours.tolevelup.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmDTO {
    private Long alarmId;
    private String fromUserId;
    private AlarmType alarmType;
    private Timestamp registeredAt;

    public static AlarmDTO fromEntity(Alarm alarm){
        return new AlarmDTO(
                alarm.getId(),
                alarm.getFromUser().getId(),
                alarm.getAlarmType(),
                alarm.getRegisteredAt()
        );
    }
}
