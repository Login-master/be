package longrun.springsecuritysessionlogin.exception;

public class UserNotFoundException extends NotFoundException{
    public UserNotFoundException(String value) {
        super(ErrorCode.USER_NOT_FOUND, value);
    }
}
