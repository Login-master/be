package longrun.springsecuritysessionlogin.service;


import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.SignupRequest;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUserId(String userId){
        return userRepository.findByUserId(userId);
    }


    public void signUp(SignupRequest request){
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(request.toEntity());
    }

    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
