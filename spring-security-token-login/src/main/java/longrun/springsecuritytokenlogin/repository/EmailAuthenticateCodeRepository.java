package longrun.springsecuritytokenlogin.repository;

import longrun.springsecuritytokenlogin.domain.EmailAuthenticateCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthenticateCodeRepository extends JpaRepository<EmailAuthenticateCode, Long> {

    Optional<EmailAuthenticateCode> findByEmail(String email);
}
