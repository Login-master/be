package longrun.springsecuritysessionlogin.exception;

import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

public class InvalidVerificationCodeException extends VerificationCodeException {
    public InvalidVerificationCodeException(String email) {
        super(ErrorCode.INVALID_VERIFICATION_CODE, email);
    }
}
