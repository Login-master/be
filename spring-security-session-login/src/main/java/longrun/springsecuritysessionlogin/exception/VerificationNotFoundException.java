package longrun.springsecuritysessionlogin.exception;

import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

public class VerificationNotFoundException extends NotFoundException {
    public VerificationNotFoundException(String value) {
        super(ErrorCode.INVALID_VERIFICATION_CODE, value);
    }
}