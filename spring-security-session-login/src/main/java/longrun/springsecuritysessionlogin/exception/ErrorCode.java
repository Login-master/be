package longrun.springsecuritysessionlogin.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    ACCESS_DENIED("접근 권한이 없습니다."),
    INVALID_INPUT_VALUE("유효성 검증에 실패하였습니다."),
    UNAUTHORIZED("인증정보가 존재하지 않습니다."),
    USER_NOT_FOUND("회원정보가 없습니다"),
    USER_ID_EXIST("이미 존재하는 아이디입니다."),
    USER_EMAIL_EXIST("이미 존재하는 이메일입니다."),
    USER_PHONE_EXIST("이미 등록된 핸드폰 번호입니다."),
    INVALID_VERIFICATION_CODE("인증번호가 올바르지 않습니다."),
    EXPIRED_VERIFICATION_CODE("인증번호가 만료되었습니다.");



    private final String message;
    public String getStatus(){
        return name();
    }

    public String getMessage(){
        return message;
    }
}
