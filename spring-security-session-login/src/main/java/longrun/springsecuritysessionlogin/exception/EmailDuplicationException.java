package longrun.springsecuritysessionlogin.exception;

import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

public class EmailDuplicationException extends DuplicationException {
    public EmailDuplicationException(final String email) {
        super(ErrorCode.USER_EMAIL_EXIST, email);
    }
}
