package longrun.springsecuritysessionlogin.exception;

import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

public class UserIdDuplicationException extends DuplicationException {
    public UserIdDuplicationException(String userId) {
        super(ErrorCode.USER_ID_EXIST, userId);
    }
}
