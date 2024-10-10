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
    private ErrorResponse(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
