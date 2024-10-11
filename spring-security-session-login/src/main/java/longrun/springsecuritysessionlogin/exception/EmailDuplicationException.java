package longrun.springsecuritysessionlogin.exception;

public class EmailDuplicationException extends DuplicationException{

    public EmailDuplicationException(final String email) {
        super(ErrorCode.USER_EMAIL_EXIST, email);
    }
}
