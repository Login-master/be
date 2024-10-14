package longrun.springsecuritysessionlogin.service;

import longrun.springsecuritysessionlogin.domain.IdRecovery;
import longrun.springsecuritysessionlogin.domain.Role;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.ForgotIdRequest;
import longrun.springsecuritysessionlogin.exception.*;
import longrun.springsecuritysessionlogin.repository.RecoveryRepository;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecoveryServiceTest {

    @Mock
    private RecoveryRepository recoveryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecoveryService recoveryService;

    @Test
    @DisplayName("랜덤인증코드 저장")
    @Transactional
    void saveVerificationCode() {
        //Given
        ForgotIdRequest request = new ForgotIdRequest("test1111@gmail.com");

        //When
        recoveryService.saveVerificationCode(request);

        //then
        verify(recoveryRepository).save(any(IdRecovery.class));
    }

    @Nested
    @DisplayName("인증코드 검증")
    class verficationCode {

        @Test
        @DisplayName("인증코드 검증 - 성공")
        @Transactional(readOnly = true)
        void validateVerificationCode_Success() {
            //Given
            String email = "test1111@gmail.com";
            IdRecovery idRecovery = IdRecovery.builder()
                    .email(email)
                    .verificationCode("1111")
                    .createdAt((LocalDateTime.now().minusMinutes(10)))//남은시간 10분
                    .build();
            User user = User.builder()
                    .userId("test1111")
                    .email(email)
                    .name("test")
                    .password("encode@@wefewfa234254")
                    .phoneNumber("1111")
                    .role(Role.USER)
                    .build();
            when(recoveryRepository.findByEmail(email)).thenReturn(Optional.of(idRecovery));
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            //When
            String maskUserId = recoveryService.validateVerificationCode(email, "1111");

            //Then
            Assertions.assertThat("test*1**").isEqualTo(maskUserId);
        }

        @Test
        @DisplayName("인증코드 검증 - 실패(인증코드 존재 x)")
        @Transactional(readOnly = true)
        void validateVerificationCode_VerificationCodeNotFoundFailure() {
            //Given
            String email = "test1111@gmail.com";
            when(recoveryRepository.findByEmail(email)).thenReturn(Optional.empty());

            //When
            Throwable throwable = catchThrowable(() -> recoveryService.validateVerificationCode(email, "1111"));

            //Then
            assertThat(throwable)
                    .isInstanceOf(VerificationNotFoundException.class)
                    .hasMessageContaining(email);
        }

        @Test
        @DisplayName("인증코드 검증 - 실패(인증코드 불일치)")
        @Transactional(readOnly = true)
        void validateVerificationCode_VerificationCodeNotSameFailure() {
            //Given
            String email = "test1111@gmail.com";
            IdRecovery idRecovery = IdRecovery.builder()
                    .email(email)
                    .verificationCode("1111")
                    .createdAt((LocalDateTime.now().minusMinutes(10)))//남은시간 10분
                    .build();
            when(recoveryRepository.findByEmail(email)).thenReturn(Optional.of(idRecovery));

            //When
            Throwable throwable = catchThrowable(() -> recoveryService.validateVerificationCode(email, "xxxx"));

            //Then
            assertThat(throwable)
                    .isInstanceOf(InvalidVerificationCodeException.class)
                    .hasMessageContaining(email);
        }

        @Test
        @DisplayName("인증코드 검증 - 실패(시간 초과)")
        @Transactional(readOnly = true)
        void validateVerificationCode_TimeOutFailure() {
            //Given
            String email = "test1111@gmail.com";
            IdRecovery idRecovery = IdRecovery.builder()
                    .email(email)
                    .verificationCode("1111")
                    .createdAt((LocalDateTime.now().minusMinutes(30)))//20분 초과
                    .build();
            when(recoveryRepository.findByEmail(email)).thenReturn(Optional.of(idRecovery));

            //When
            Throwable throwable = catchThrowable(() -> recoveryService.validateVerificationCode(email, "1111"));

            //Then
            assertThat(throwable)
                    .isInstanceOf(ExpiredVerificationCodeException.class)
                    .hasMessageContaining(email);
        }
    }
}