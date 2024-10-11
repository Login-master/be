package longrun.springsecuritysessionlogin.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import longrun.springsecuritysessionlogin.exception.ErrorCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private String value;

    @Builder
    private ErrorResponse(final int status, final String code, final String message, final String value) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.value = value;
    }
}
