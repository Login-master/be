//package longrun.springsecuritysessionlogin.security;
//
//import longrun.springsecuritysessionlogin.config.SecurityConfig;
//import longrun.springsecuritysessionlogin.dto.request.LoginRequest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.BDDMockito.given;
//
//@WebMvcTest(value = CustomAuthenticationFilter.class,includeFilters = @ComponentScan.Filter(SecurityConfig.class))
//class CustomAuthenticationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CustomUserDetailsService customUserDetailsService;
//
//
//
//    @Test
//    @WithMockUser
//    void loginTest(){
//        //Given
//        LoginRequest request = LoginRequest.builder()
//                .userId("test1111")
//                .password("@@Test1234").build();
//        given(customUserDetailsService.loadUserByUsername("test1111")).willReturn()
//        //When
//
//        //Then
//
//    }
//
//
//}