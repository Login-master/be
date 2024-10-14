package longrun.springsecuritysessionlogin.exception;

public class InvalidVerificationCodeException extends VerificationCodeException{
    public InvalidVerificationCodeException(String email) {
        super(ErrorCode.INVALID_VERIFICATION_CODE, email);
    }
}
