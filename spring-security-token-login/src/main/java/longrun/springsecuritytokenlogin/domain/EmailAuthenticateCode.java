package longrun.springsecuritytokenlogin.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmailAuthenticateCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본 키 생성을 데이터베이스에 위임, MySQL에서 사용
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String authenticateCode;
}
