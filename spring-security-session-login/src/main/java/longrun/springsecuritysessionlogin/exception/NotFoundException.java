package longrun.springsecuritysessionlogin.exception;


import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException{

    public String value;
    public NotFoundException(ErrorCode errorCode, String value) {
        super(errorCode, value);
        this.value = value;
    }
}
