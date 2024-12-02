package longrun.springsecuritysessionlogin.service;


import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.domain.IdRecovery;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.ForgotIdRequest;
import longrun.springsecuritysessionlogin.exception.*;
import longrun.springsecuritysessionlogin.repository.RecoveryRepository;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecoveryService {

    private final RecoveryRepository recoveryRepository;
    private final UserRepository userRepository;

    private String createVerificationCode() {
        return "1111";
    }

    @Transactional
    public void saveVerificationCode(ForgotIdRequest request) {
        checkVerificationCode(request.getEmail());
        IdRecovery idRecovery = IdRecovery.builder()
                .email(request.getEmail())
                .verificationCode(createVerificationCode())
                .createdAt(LocalDateTime.now())
                .build();

        recoveryRepository.save(idRecovery);
    }

    @Transactional
    public void checkVerificationCode(String email) {
        recoveryRepository.findByEmail(email).ifPresent(recoveryRepository::delete);
    }

    @Transactional
    public void deleteVerificationCode(IdRecovery idRecovery) {
        recoveryRepository.delete(idRecovery);
    }

    @Transactional
    public String validateVerificationCode(String email, String verificationCode) {
        IdRecovery idRecovery = recoveryRepository.findByEmail(email)
                .orElseThrow(() -> new VerificationNotFoundException(email));
        if (!Objects.equals(idRecovery.getVerificationCode(), verificationCode)) {
            throw new InvalidVerificationCodeException(email);
        }
        deleteVerificationCode(idRecovery);
        // 인증시간 초과했나 비교 (제한 20분)
        Duration diff = Duration.between(idRecovery.getCreatedAt(), LocalDateTime.now());
        long diffMin = diff.toMinutes();
        if (diffMin < 20) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(email));
            return maskUserId(user.getUserId());
        }
        throw new ExpiredVerificationCodeException(email);// 시간초과 오류
    }

    public static String maskUserId(String userId) {
        int length = userId.length();

        StringBuilder maskedId = new StringBuilder(userId);
        maskedId.setCharAt(length / 2, '*');
        maskedId.setCharAt(length - 1, '*');
        maskedId.setCharAt(length - 2, '*');

        return maskedId.toString();
    }
}
