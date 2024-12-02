package longrun.springsecuritysessionlogin.exception;

import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String value) {
        super(ErrorCode.USER_NOT_FOUND, value);
    }
}
