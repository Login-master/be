package longrun.springsecuritytokenlogin.service;

import lombok.RequiredArgsConstructor;
import longrun.springsecuritytokenlogin.domain.User;
import longrun.springsecuritytokenlogin.dto.response.AdminResponse;
import longrun.springsecuritytokenlogin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    // 모든 유저 정보를 반환하는 메서드
    public AdminResponse getAllUsers() {
        AdminResponse adminResponse = new AdminResponse(userRepository.findAll());
        return adminResponse; // UserRepository의 메서드 호출
    }
}
