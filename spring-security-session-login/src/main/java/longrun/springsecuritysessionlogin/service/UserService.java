package longrun.springsecuritysessionlogin.service;


import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.SignupRequest;
import longrun.springsecuritysessionlogin.exception.*;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND,email));
    }

    public void signUp(SignupRequest request){
        validateDuplicateUser(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(request.toEntity());
    }

    private void validateDuplicateUser(SignupRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailDuplicationException(request.getEmail());
        }
        if(userRepository.findByUserId(request.getUserId()).isPresent()){
            throw new UserIdDuplicationException(request.getUserId());
        }
        if(userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
            throw new PhoneNumberDuplicationException(request.getPhoneNumber());
        }
    }
}
