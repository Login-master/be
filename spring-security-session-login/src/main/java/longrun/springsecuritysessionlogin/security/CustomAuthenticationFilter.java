package longrun.springsecuritysessionlogin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.dto.request.LoginRequest;
import longrun.springsecuritysessionlogin.dto.response.ErrorResponse;
import longrun.springsecuritysessionlogin.exception.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import javax.lang.model.type.ErrorType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/member/login", "POST");
    private boolean postOnly = true;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }


    //로그인 요청 처리 및 인증 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            LoginRequest loginRequest = objectMapper.readValue(messageBody, LoginRequest.class);

            // JSON 타입으로 변환한 후 인증 객체 생성
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                    loginRequest.getUserId(), loginRequest.getPassword());

            this.setDetails(request, authRequest);

            // 인증 시도
            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (AuthenticationException e) {
            // 인증 실패 시 응답
            setErrorResponse(response, ErrorCode.USER_NOT_FOUND);
            return null;
        }
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    // 예외 응답을 HttpServletResponse에 설정하는 메서드
    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse)); // JSON으로 변환해 응답
    }

}

