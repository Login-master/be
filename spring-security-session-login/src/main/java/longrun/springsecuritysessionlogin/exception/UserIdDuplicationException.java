package longrun.springsecuritysessionlogin.exception;

public class UserIdDuplicationException extends DuplicationException {
    public UserIdDuplicationException(String userId) {
        super(ErrorCode.USER_ID_EXIST, userId);
    }
}
