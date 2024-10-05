package longrun.springsecuritytokenlogin.dto.response;

import longrun.springsecuritytokenlogin.domain.User; // User 도메인 클래스
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private User user;  // User 정보
    private String token; // JWT 토큰
}
