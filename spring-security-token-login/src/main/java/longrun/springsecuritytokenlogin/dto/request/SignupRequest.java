package longrun.springsecuritytokenlogin.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import longrun.springsecuritytokenlogin.domain.Role;
import longrun.springsecuritytokenlogin.domain.User;
import org.hibernate.validator.constraints.Range;


@Data
public class SignupRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 6, max = 12, message = "아이디의 길이가 올바르지 않습니다.")
    private String userId;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호의 길이가 올바르지 않습니다.")
    @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "비밀번호는 특수 문자가 하나 이상 포함되어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "이름은 특수문자를 제외한 2~10자리여야 합니다.")
    private String name;


    private String phoneNumber;

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .name(name)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .build();
    }


}