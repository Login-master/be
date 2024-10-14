package longrun.springsecuritysessionlogin.exception;

public class ExpiredVerificationCodeException extends VerificationCodeException{
    public ExpiredVerificationCodeException(String email) {
        super(ErrorCode.EXPIRED_VERIFICATION_CODE, email);
    }
}
