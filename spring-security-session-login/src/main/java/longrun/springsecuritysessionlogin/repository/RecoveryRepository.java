package longrun.springsecuritysessionlogin.repository;

import longrun.springsecuritysessionlogin.domain.IdRecovery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryRepository extends JpaRepository<IdRecovery,Long> {
}
