package longrun.springsecuritysessionlogin.service;

import longrun.springsecuritysessionlogin.domain.Role;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.SignupRequest;
import longrun.springsecuritysessionlogin.exception.*;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId("test1111")
                .email("test1111@gmail.com")
                .name("test")
                .password("@@wefewfa234254")
                .phoneNumber("1111")
                .role(Role.USER)
                .build();
    }

    @Nested
    @DisplayName("이메일 찾기")
    class findByEmail_Test {
        @Test
        @DisplayName("해당 이메일이 존재할 때")
        void findByEmail_UserFound() {
            //Given
            String email = "test1111@gmail.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            //When
            User foundUser = userService.findByEmail(email);

            //Then
            assertThat(foundUser.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("해당 이메일이 존재하지 않을 떄")
        void findByEmail_UserNotFound() {
            //Given
            String email = "xxxx1111@gmail.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            //When
            Throwable throwable = catchThrowable(() -> userService.findByEmail(email));

            //Then
            assertThat(throwable)
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(email);
        }
    }

    @Nested
    @DisplayName("회원가입")
    class signUp_Test {
        @Test
        @DisplayName("회원가입 성공")
        void signUp_Success() {
            // Given
            SignupRequest request = new SignupRequest("test1111", "test1111@gmail.com", "@@Test11111", "test", "01011111111");
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
            when(userRepository.findByUserId(request.getUserId())).thenReturn(Optional.empty());
            when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());

            // When
            userService.signUp(request);

            // Then
            verify(passwordEncoder).encode("@@Test11111");
            verify(userRepository).save(any(User.class)); // User 객체가 저장되었는지 확인
        }

        @Test
        @DisplayName("회원가입 실패 - 중복이메일")
        void signUp_DuplicateEmail() {
            // Given
            SignupRequest request = new SignupRequest("test1111", "test1111@gmail.com", "@@Test11111", "test", "01011111111");
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

            //When
            Throwable throwable = catchThrowable(() -> userService.signUp(request));

            //Then
            assertThat(throwable)
                    .isInstanceOf(EmailDuplicationException.class)
                    .hasMessageContaining(request.getEmail());
        }

        @Test
        @DisplayName("회원가입 실패 - 중복아이디")
        void signUp_DuplicateUserId() {
            // Given
            SignupRequest request = new SignupRequest("test1111", "test1111@gmail.com", "@@Test11111", "test", "01011111111");
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
            when(userRepository.findByUserId(request.getUserId())).thenReturn(Optional.of(user));

            //When
            Throwable throwable = catchThrowable(() -> userService.signUp(request));

            //Then
            assertThat(throwable)
                    .isInstanceOf(UserIdDuplicationException.class)
                    .hasMessageContaining(request.getUserId());
        }

        @Test
        @DisplayName("회원가입 실패 - 중복전화번호")
        void signUp_DuplicatePhoneNumber() {
            // Given
            SignupRequest request = new SignupRequest("test1111", "test1111@gmail.com", "@@Test11111", "test", "01011111111");
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
            when(userRepository.findByUserId(request.getUserId())).thenReturn(Optional.empty());
            when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(user));

            //When
            Throwable throwable = catchThrowable(() -> userService.signUp(request));

            //Then
            assertThat(throwable)
                    .isInstanceOf(PhoneNumberDuplicationException.class)
                    .hasMessageContaining(request.getPhoneNumber());
        }
    }

}