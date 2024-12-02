package longrun.springsecuritysessionlogin.exception;

import lombok.Getter;
import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

@Getter
public class BusinessException extends RuntimeException{

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    } // 예외 추적 x
}
