package longrun.springsecuritysessionlogin.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgotIdResponse {
    private int status;
    private String code;
    private String message;
    private String value;

    @Builder
    private ForgotIdResponse(final int status, final String code, final String message, final String value) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.value = value;
    }
}
