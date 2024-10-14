package longrun.springsecuritysessionlogin.exception;

public class VerificationNotFoundException extends NotFoundException{
    public VerificationNotFoundException(String value) {
        super(ErrorCode.INVALID_VERIFICATION_CODE, value);
    }
}