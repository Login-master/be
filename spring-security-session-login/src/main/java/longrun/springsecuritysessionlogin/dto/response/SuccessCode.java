package longrun.springsecuritysessionlogin.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SuccessCode {
    //COMMON

    //USER
    LOGIN_SUCCESS(200, "USER-001", "로그인 성공"),
    USER_REGISTERED(201, "USER-002", "회원가입 성공"),

    //RECOVERY
    CREATE_VERIFICATION_CODE(201, "RECOVERY-001", "인증번호 생성"),
    VALID_VERIFICATION_CODE(200, "RECOVERY-002", "인증번호 검증 성공");


    private final int status;
    private final String code;
    private final String message;

}
