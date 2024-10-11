package longrun.springsecuritysessionlogin.exception;

public class PhoneNumberDuplicationException extends DuplicationException{

    public PhoneNumberDuplicationException(String phoneNumber) {
        super(ErrorCode.USER_PHONE_EXIST, phoneNumber);
    }
}
