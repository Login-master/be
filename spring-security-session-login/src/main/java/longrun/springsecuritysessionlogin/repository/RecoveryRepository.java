package longrun.springsecuritysessionlogin.repository;

import longrun.springsecuritysessionlogin.domain.IdRecovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecoveryRepository extends JpaRepository<IdRecovery,Long> {
    Optional<IdRecovery> findByEmail(String email);
}
