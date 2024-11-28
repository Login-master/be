package longrun.springsecuritysessionlogin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SignUpResponse {
    private int status;
    private String code;
    private String message;
    private String value;

    @Builder
    private SignUpResponse(final int status, final String code, final String message, final String value) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.value = value;
    }
}
