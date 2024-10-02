package longrun.springsecuritysessionlogin.security;

import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException(userId));
        return new CustomUserDetails(user);
    }
}
