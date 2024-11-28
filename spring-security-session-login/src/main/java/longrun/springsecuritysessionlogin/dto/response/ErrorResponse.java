package longrun.springsecuritysessionlogin.dto.response;

import lombok.*;

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
        this.code = code;
        this.message = message;
        this.value = value;
    }
}
