package huan.backend.exception;

import huan.backend.enumerate.ErrorCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); 
        this.errorCode = errorCode;    
    }
}