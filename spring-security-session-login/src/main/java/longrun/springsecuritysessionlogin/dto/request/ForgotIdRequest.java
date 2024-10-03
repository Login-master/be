package longrun.springsecuritysessionlogin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotIdRequest {
    @NotBlank(message = "이메일은 필수 입력값입니다")
    private String email;
}
