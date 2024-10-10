package longrun.springsecuritytokenlogin.service;

import lombok.RequiredArgsConstructor;
import longrun.springsecuritytokenlogin.domain.EmailAuthenticateCode;
import longrun.springsecuritytokenlogin.domain.User;
import longrun.springsecuritytokenlogin.dto.request.EmailAuthenticateCodeRequest;
import longrun.springsecuritytokenlogin.dto.request.SignupRequest;
import longrun.springsecuritytokenlogin.exception.RestApiException;
import longrun.springsecuritytokenlogin.exception.UserErrorCode;
import longrun.springsecuritytokenlogin.repository.EmailAuthenticateCodeRepository;
import longrun.springsecuritytokenlogin.repository.UserRepository;
import longrun.springsecuritytokenlogin.security.JWTUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder BCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final EmailAuthenticateCodeRepository emailAuthenticateCodeRepository;

    public void signUp(SignupRequest signupRequest){
        signupRequest.setPassword(BCryptPasswordEncoder.encode(signupRequest.getPassword()));

        // 아이디가 이미 존재하는 경우 예외 던지기
        userRepository.findByUserId(signupRequest.getUserId())
                .ifPresent(user -> {
                    throw new RestApiException(UserErrorCode.USER_ALREADY_EXISTS);
                });

        // 이메일이 이미 등록된 경우 예외 던지기
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(user -> {
                    throw new RestApiException(UserErrorCode.EMAIL_ALREADY_REGISTERED);
                });

        // 사용자 저장
        userRepository.save(signupRequest.toEntity());
    }

    public String sendAuthenticateCode(EmailAuthenticateCodeRequest emailAuthenticateCodeRequest){
        Optional<EmailAuthenticateCode> byEmail = emailAuthenticateCodeRepository.findByEmail(emailAuthenticateCodeRequest.getEmail());
        String newAuthenticateCode = "111111"; // 새 인증 코드 생성 메서드

        if (byEmail.isPresent()) {
            EmailAuthenticateCode existingCode = byEmail.get();
            EmailAuthenticateCode updatedCode = new EmailAuthenticateCode(
                    existingCode.getId(),
                    existingCode.getEmail(),
                    newAuthenticateCode
            );
            emailAuthenticateCodeRepository.save(updatedCode);
        } else {
            EmailAuthenticateCode newCode = EmailAuthenticateCode.builder()
                    .email(emailAuthenticateCodeRequest.getEmail())
                    .authenticateCode(newAuthenticateCode)
                    .build();
            emailAuthenticateCodeRepository.save(newCode);
        }

        return newAuthenticateCode;
    }

    public String verifyAuthenticateCode(EmailAuthenticateCodeRequest emailAuthenticateCodeRequest){
        // 이메일이 인증되지 않았을 경우 예외 발생
        EmailAuthenticateCode byEmail = emailAuthenticateCodeRepository.findByEmail(emailAuthenticateCodeRequest.getEmail())
                .orElseThrow(() -> new RestApiException(UserErrorCode.EMAIL_NOT_AUTHENTICATED));

        // 인증 코드가 일치하지 않는 경우 예외 발생
        if(!byEmail.getAuthenticateCode().equals(emailAuthenticateCodeRequest.getAuthenticateCode())){
            throw new RestApiException(UserErrorCode.AUTHENTICATION_CODE_MISMATCH);
        }

        // 이메일이 회원가입된 기록이 없는 경우 예외 발생
        User user = userRepository.findByEmail(byEmail.getEmail())
                .orElseThrow(() -> new RestApiException(UserErrorCode.EMAIL_NOT_REGISTERED));

        String encodedUserId = userIdEncoder(user);
        return encodedUserId;
    }

    private String userIdEncoder(User user){
        String userId = user.getUserId();
        int length = userId.length();
        int start = (length - 2) / 2;
        StringBuilder maskedUserId = new StringBuilder(userId);
        maskedUserId.replace(start, start + 2, "**");
        return maskedUserId.toString();
    }
}
