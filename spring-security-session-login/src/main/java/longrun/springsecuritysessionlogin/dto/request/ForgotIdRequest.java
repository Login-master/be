package longrun.springsecuritysessionlogin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotIdRequest {
    @NotBlank(message = "이메일은 필수 입력값입니다")
    private String email;
}
