package longrun.springsecuritysessionlogin.service;


import longrun.springsecuritysessionlogin.domain.IdRecovery;
import longrun.springsecuritysessionlogin.dto.request.ForgotIdRequest;
import longrun.springsecuritysessionlogin.repository.RecoveryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RecoveryService {

    private RecoveryRepository recoveryRepository;

    private String createVerificationCode(){
        return "1111";
    }

    public void saveVerificationCode(ForgotIdRequest request){
        IdRecovery idRecovery = IdRecovery.builder()
                .email(request.getEmail())
                .verificationCode(createVerificationCode())
                .createdAt(LocalDateTime.now())
                .build();

        recoveryRepository.save(idRecovery);
    }
}
