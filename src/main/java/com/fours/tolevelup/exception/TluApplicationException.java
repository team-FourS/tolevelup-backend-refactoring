package com.fours.tolevelup.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TluApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public TluApplicationException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage(){
        if(message == null){
            return errorCode.getMessage();
        }
        return String.format("%s. %s",errorCode.getMessage(),message);
    }

}
