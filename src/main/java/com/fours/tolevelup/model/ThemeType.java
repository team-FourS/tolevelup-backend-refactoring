package com.fours.tolevelup.model;

import com.fours.tolevelup.exception.ErrorCode;
import com.fours.tolevelup.exception.TluApplicationException;

public enum ThemeType {
    DAILY,
    WEEKLY;

    public static ThemeType of(String request) {
        if (request.equals("DAILY")) return DAILY;
        if (request.equals("WEEKLY")) return WEEKLY;
        throw new TluApplicationException(ErrorCode.TYPE_NOT_FOUND);
    }
}
