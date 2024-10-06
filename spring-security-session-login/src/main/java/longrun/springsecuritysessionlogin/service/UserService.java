package longrun.springsecuritysessionlogin.service;


import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.dto.request.SignupRequest;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignupRequest request){
        validateDuplicateUser(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(request.toEntity());
    }

    private void validateDuplicateUser(SignupRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalStateException("이미 가입된 이메일입니다. ");
        }
        if(userRepository.findByUserId(request.getUserId()).isPresent()){
            throw new IllegalStateException("이미 가입된 아이디입니다. ");
        }
        if(userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
            throw new IllegalStateException("이미 가입된 전화번호입니다. ");
        }
    }
}
