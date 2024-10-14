package longrun.springsecuritysessionlogin.service;

import longrun.springsecuritysessionlogin.domain.IdRecovery;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.ForgotIdRequest;
import longrun.springsecuritysessionlogin.repository.RecoveryRepository;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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
    void saveVerificationCode() {
        //Given
        ForgotIdRequest request = new ForgotIdRequest("test1111@gmail.com");

        //When
        recoveryService.saveVerificationCode(request);

        //then
        verify(recoveryRepository).save(any(IdRecovery.class));
    }

    @Test
    @DisplayName("")
    void validateVerificationCode() {
    }

    @Test
    void maskUserId() {
    }
}