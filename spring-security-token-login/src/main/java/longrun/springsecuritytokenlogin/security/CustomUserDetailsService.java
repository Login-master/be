package longrun.springsecuritytokenlogin.security;

import lombok.RequiredArgsConstructor;
import longrun.springsecuritytokenlogin.domain.User;
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

        Optional<User> user = userRepository.findByUserId(userId);
        if(user != null) {
            return new CustomUserDetails(user.get());
        }
        return null;
    }
}