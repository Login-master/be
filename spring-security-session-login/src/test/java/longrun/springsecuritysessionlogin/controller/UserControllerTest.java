package longrun.springsecuritysessionlogin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import longrun.springsecuritysessionlogin.config.CorsConfig;
import longrun.springsecuritysessionlogin.config.SecurityConfig;
import longrun.springsecuritysessionlogin.domain.Role;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.ForgotIdRequest;
import longrun.springsecuritysessionlogin.dto.request.SignUpRequest;
import longrun.springsecuritysessionlogin.dto.response.SignUpResponse;
import longrun.springsecuritysessionlogin.exception.DuplicationException;
import longrun.springsecuritysessionlogin.exception.EmailDuplicationException;
import longrun.springsecuritysessionlogin.exception.ErrorCode;
import longrun.springsecuritysessionlogin.exception.ExpiredVerificationCodeException;
import longrun.springsecuritysessionlogin.service.RecoveryService;
import longrun.springsecuritysessionlogin.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
//@AutoConfigureMockMvc(addFilters = false) //Filter 등록 x(인증 안함)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private UserService userService;

    @MockBean
    private RecoveryService recoveryService;

    @Test
    @WithMockUser // 가짜 사용자 인증 정보 mocking
    @DisplayName("회원가입 성공 로직")
    void postSignUpTest() throws Exception {
        //Given
        SignUpRequest request = new SignUpRequest("test1111", "test1111@gmail.com", "@@Test11111", "test", "01011111111");
        given(userService.signUp(request)).willReturn(User.builder()
                .userId("test1111")
                .email("test1111@gmail.com")
                .name("test")
                .password("encode@@Test11111") //암호화 되었다고 가정
                .phoneNumber("01011111111")
                .role(Role.USER)
                .build());
        //When
        ResultActions actions =
                mockMvc.perform(
                        post("/member/sign-up")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                                .with(csrf())

                );
        //Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SignUp success"))
                .andExpect(jsonPath("$.value").value(request.getUserId()))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 실패 로직 - 이메일 중복")
    void postSignUpTest_Failure() throws Exception {
        //Given
        SignUpRequest request = new SignUpRequest("test1111", "test1111@gmail.com", "@@Test11111", "test", "01011111111");
        given(userService.signUp(request)).willThrow(new EmailDuplicationException(request.getEmail()));
        //When
        ResultActions actions =
                mockMvc.perform(
                        post("/member/sign-up")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                                .with(csrf())

                );
        //Then
        actions
                .andExpect(status().is(ErrorCode.USER_EMAIL_EXIST.getStatus()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_EMAIL_EXIST.getMessage()))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("인증코드 생성 - 성공")
    void createIdRecoveryCode_Success() throws Exception{
        //Given
        ForgotIdRequest request = new ForgotIdRequest("test1111@gmail.com");
        given(userService.findByEmail(request.getEmail())).willReturn(User.builder()
                .userId("test1111")
                .email("test1111@gmail.com")
                .name("test")
                .password("encode@@Test11111") //암호화 되었다고 가정
                .phoneNumber("01011111111")
                .role(Role.USER)
                .build());
        doNothing().when(recoveryService).saveVerificationCode(request);
        // When
        ResultActions actions = mockMvc.perform(post("/member/forgot-id")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"email\": \"test1111@gmail.com\"}")
                                        .with(csrf()));

        //Then
        actions
                .andExpect(status().isOk())
                .andExpect(content().string("create recoveryCode"))
                .andDo(print());
    }


    @Test
    @WithMockUser
    @DisplayName("아이디 찾기 인증 성공")
    void sendRecoveryCode_Success() throws Exception {
        //Given
        String email = "test1111@gmail.com";
        String verificationCode = "1111";
        given(recoveryService.validateVerificationCode(email,verificationCode)).willReturn("test*1**");

        //When
        ResultActions actions = mockMvc.perform(get("/member/find-id-verify")
                                            .param("email", email)
                                            .param("verificationCode", verificationCode)
                                            .with(csrf()));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().string("test*1**"))
                .andDo(print());
    }
    @Test
    @WithMockUser
    @DisplayName("아이디 찾기 인증 실패 - 인증시간경과")
    void sendRecoveryCode_Failure() throws Exception {
        //Given
        String email = "test1111@gmail.com";
        String verificationCode = "1111";
        given(recoveryService.validateVerificationCode(email,verificationCode)).willThrow(new ExpiredVerificationCodeException(email));

        //When
        ResultActions actions = mockMvc.perform(get("/member/find-id-verify")
                .param("email", email)
                .param("verificationCode", verificationCode)
                .with(csrf()));

        //then
        actions
                .andExpect(status().is(ErrorCode.EXPIRED_VERIFICATION_CODE.getStatus()))
                .andExpect(jsonPath("$.message").value(ErrorCode.EXPIRED_VERIFICATION_CODE.getMessage()))
                .andDo(print());
    }
}