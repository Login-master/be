package longrun.springsecuritysessionlogin.exception;

public class VerificationCodeException extends BusinessException{

    public String email;
    public VerificationCodeException(ErrorCode errorCode, String email) {
        super(errorCode, email);
        this.email = email;
    }
}
