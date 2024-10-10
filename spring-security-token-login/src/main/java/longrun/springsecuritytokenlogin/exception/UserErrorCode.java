package longrun.springsecuritytokenlogin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    EMAIL_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 회원가입된 이메일입니다."),
    EMAIL_NOT_AUTHENTICATED(HttpStatus.BAD_REQUEST, "인증된 적 없는 메일입니다."),
    AUTHENTICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    EMAIL_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "회원가입 기록이 없는 이메일입니다."),
    ;


    private final HttpStatus httpStatus;
    private final String message;
}
