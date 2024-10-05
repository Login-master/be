package longrun.springsecuritytokenlogin.domain;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본 키 생성을 데이터베이스에 위임, MySQL에서 사용
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Role role;

    public User(String userId, String password, String role){
        this.userId = userId;
        this.password = password;
        if(role.equals("ADMIN")){
            this.role = Role.ADMIN;
        }else{
            this.role = Role.USER;
        }
    }
}