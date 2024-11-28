package longrun.springsecuritysessionlogin.exception;

import longrun.springsecuritysessionlogin.dto.response.ErrorCode;

public class PhoneNumberDuplicationException extends DuplicationException {
    public PhoneNumberDuplicationException(String phoneNumber) {
        super(ErrorCode.USER_PHONE_EXIST, phoneNumber);
    }
}
