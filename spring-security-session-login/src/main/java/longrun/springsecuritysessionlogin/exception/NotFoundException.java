package longrun.springsecuritysessionlogin.exception;


import lombok.Getter;
import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

@Getter
public class NotFoundException extends BusinessException {

    public String value;

    public NotFoundException(ErrorCode errorCode, String value) {
        super(errorCode, value);
        this.value = value;
    }
}
