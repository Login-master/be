package longrun.springsecuritytokenlogin.service;

import lombok.RequiredArgsConstructor;
import longrun.springsecuritytokenlogin.domain.EmailAuthenticateCode;
import longrun.springsecuritytokenlogin.domain.User;
import longrun.springsecuritytokenlogin.dto.request.EmailAuthenticateCodeRequest;
import longrun.springsecuritytokenlogin.dto.request.LoginRequest;
import longrun.springsecuritytokenlogin.dto.request.SignupRequest;
import longrun.springsecuritytokenlogin.dto.response.LoginResponse;
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
                .ifPresent(user -> { throw new IllegalArgumentException("이미 존재하는 아이디입니다."); });
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(user -> { throw new IllegalArgumentException("이미 회원가입된 이메일입니다"); });
        userRepository.save(signupRequest.toEntity());
    }

    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 아이디입니다."));
        if (!BCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        String token = jwtUtil.createJwt(user.getUserId(), String.valueOf(user.getRole()), 1000 * 60 * 60L);
        LoginResponse loginResponse = new LoginResponse(user, token);

        return loginResponse;
    }

    public String sendAuthenticateCode(EmailAuthenticateCodeRequest emailAuthenticateCodeRequest){
        Optional<EmailAuthenticateCode> byEmail = emailAuthenticateCodeRepository.findByEmail(emailAuthenticateCodeRequest.getEmail());
        String newAuthenticateCode = "111111"; // 새 인증 코드 생성 메서드

        if (byEmail.isPresent()) {
            // 이메일이 존재하면 인증 코드만 업데이트
            EmailAuthenticateCode existingCode = byEmail.get();
            // 새로운 EmailAuthenticateCode 객체 생성
            EmailAuthenticateCode updatedCode = new EmailAuthenticateCode(
                    existingCode.getId(), // 기존 ID 유지
                    existingCode.getEmail(), // 기존 이메일 유지
                    newAuthenticateCode // 새로운 인증 코드로 업데이트
            );
            emailAuthenticateCodeRepository.save(updatedCode); // 업데이트된 엔티티 저장
        } else {
            // 이메일이 존재하지 않으면 새로 생성
            EmailAuthenticateCode newCode = EmailAuthenticateCode.builder()
                    .email(emailAuthenticateCodeRequest.getEmail())
                    .authenticateCode(newAuthenticateCode)
                    .build();
            emailAuthenticateCodeRepository.save(newCode); // 새 엔티티 저장
        }

        return newAuthenticateCode;
    }

    public String verifyAuthenticateCode(EmailAuthenticateCodeRequest emailAuthenticateCodeRequest){
        EmailAuthenticateCode byEmail = emailAuthenticateCodeRepository.findByEmail(emailAuthenticateCodeRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("인증된 적 없는 메일입니다."));
        if(!byEmail.getAuthenticateCode().equals(emailAuthenticateCodeRequest.getAuthenticateCode())){
            throw new IllegalArgumentException("인증번호가 일치하지않습니다.");
        }

        User user = userRepository.findByEmail(byEmail.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원가입 기록이 없는 이메일입니다."));
        String encodedUserId = userIdEncoder(user);
        return encodedUserId;
    }

    private String userIdEncoder(User user){
        String userId = user.getUserId();
        int length = userId.length();
        // 중간 두 글자의 시작 인덱스 계산
        int start = (length - 2) / 2;

        // 중간 두 글자를 *로 마스킹
        StringBuilder maskedUserId = new StringBuilder(userId);
        maskedUserId.replace(start, start + 2, "**"); // 중간 두 글자 마스킹

        return maskedUserId.toString();
    }
}

