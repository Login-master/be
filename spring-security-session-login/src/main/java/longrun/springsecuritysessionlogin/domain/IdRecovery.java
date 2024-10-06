package longrun.springsecuritysessionlogin.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="recovery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdRecovery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String verificationCode;
    private LocalDateTime createdAt;

    @Builder
    public IdRecovery(String email, String verificationCode, LocalDateTime createdAt){
        this.email = email;
        this.verificationCode = verificationCode;
        this.createdAt = createdAt;
    }
}
