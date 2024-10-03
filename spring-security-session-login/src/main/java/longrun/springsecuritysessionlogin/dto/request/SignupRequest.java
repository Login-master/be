package longrun.springsecuritysessionlogin.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import longrun.springsecuritysessionlogin.domain.Role;
import longrun.springsecuritysessionlogin.domain.User;

@Data
public class SignupRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 6, max = 12)
    private String userId;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8)
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
