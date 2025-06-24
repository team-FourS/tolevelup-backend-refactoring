package com.fours.tolevelup.model;

import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;

public enum ThemeType {
    DAILY,
    WEEKLY;

    public static ThemeType of(String request) {
        return switch (request) {
            case "DAILY" -> DAILY;
            case "WEEKLY" -> WEEKLY;
            default -> throw new TluApplicationException(ErrorCode.TYPE_NOT_FOUND);
        };
    }
}
