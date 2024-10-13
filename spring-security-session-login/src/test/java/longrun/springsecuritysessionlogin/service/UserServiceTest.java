package longrun.springsecuritysessionlogin.service;

import longrun.springsecuritysessionlogin.domain.Role;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.SignupRequest;
import longrun.springsecuritysessionlogin.exception.ErrorCode;
import longrun.springsecuritysessionlogin.exception.UserNotFoundException;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Mockito 목 객체 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("해당 이메일이 존재할 때")
    void findByEmail_UserFound() {
        //Given
        String email = "test1111@gmail.com";
        User user = User.builder()
                .userId("test1111")
                .email(email)
                .name("test")
                .password("@@wefewfa234254")
                .phoneNumber("1111")
                .role(Role.USER)
                .build();
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