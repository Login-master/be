package longrun.springsecuritysessionlogin.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import longrun.springsecuritysessionlogin.dto.response.LoginResponse;
import longrun.springsecuritysessionlogin.dto.response.SignUpResponse;
import longrun.springsecuritysessionlogin.dto.response.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 메시지 및 사용자 ID 생성
        SuccessCode successCode = SuccessCode.LOGIN_SUCCESS;
        LoginResponse loginResponse = LoginResponse.builder()
                .status(successCode.getStatus())
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .value(authentication.getName()).build();

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse)); // JSON 형식으로 변환하여 응답
    }
}
