package longrun.springsecuritysessionlogin.exception;


import lombok.Getter;
import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

@Getter
public class DuplicationException extends BusinessException {

    public String value;

    public DuplicationException(ErrorCode errorCode, String value) {
        super(errorCode, value);
        this.value = value;
    }
}


