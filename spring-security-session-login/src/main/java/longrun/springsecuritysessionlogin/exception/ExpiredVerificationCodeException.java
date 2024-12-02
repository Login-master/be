package longrun.springsecuritysessionlogin.exception;

import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

public class ExpiredVerificationCodeException extends VerificationCodeException {
    public ExpiredVerificationCodeException(String email) {
        super(ErrorCode.EXPIRED_VERIFICATION_CODE, email);
    }
}
