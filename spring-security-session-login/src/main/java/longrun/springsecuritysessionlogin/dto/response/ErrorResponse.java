package longrun.springsecuritysessionlogin.dto.response;

import lombok.*;
import longrun.springsecuritysessionlogin.exception.ErrorCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;
    private String code;
    private String message;

    @Builder
    private ErrorResponse(final ErrorCode code) {
        this.status = code.getStatus();
        this.message = code.getMessage();
        this.code = code.getCode();
    }
}
