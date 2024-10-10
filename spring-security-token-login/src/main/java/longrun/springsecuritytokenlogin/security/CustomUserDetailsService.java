package longrun.springsecuritytokenlogin.security;

import lombok.RequiredArgsConstructor;
import longrun.springsecuritytokenlogin.domain.User;
import longrun.springsecuritytokenlogin.exception.CommonErrorCode;
import longrun.springsecuritytokenlogin.exception.RestApiException;
import longrun.springsecuritytokenlogin.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println(1);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userId));
        return new CustomUserDetails(user);
    }

}