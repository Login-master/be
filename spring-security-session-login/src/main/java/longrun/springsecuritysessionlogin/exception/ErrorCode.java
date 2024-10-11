package longrun.springsecuritysessionlogin.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    //COMMON
    ACCESS_DENIED(403,"COMMON-001","접근 권한이 없습니다."),
    INVALID_INPUT_VALUE(400,"COMMON-002","유효성 검증에 실패하였습니다."),
    UNAUTHORIZED(401,"COMMON-003","인증정보가 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(404,"COMMON-004", "Server Error"),

    //USER
    USER_NOT_FOUND(404,"USER-001","회원정보가 없습니다"),
    USER_ID_EXIST(400,"USER-002","이미 존재하는 아이디입니다."),
    USER_EMAIL_EXIST(400,"USER-003","이미 존재하는 이메일입니다."),
    USER_PHONE_EXIST(400,"USER-004","이미 등록된 핸드폰 번호입니다."),

    //RECOVERY
    INVALID_VERIFICATION_CODE(404,"RECOVERY-001","인증번호가 올바르지 않습니다."),
    EXPIRED_VERIFICATION_CODE(404,"RECOVERY-002","인증번호가 만료되었습니다.");



    private final int status;
    private final String code;
    private final String message;

}
