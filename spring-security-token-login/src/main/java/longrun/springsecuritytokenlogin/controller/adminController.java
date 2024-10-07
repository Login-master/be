package longrun.springsecuritytokenlogin.controller;

import lombok.RequiredArgsConstructor;
import longrun.springsecuritytokenlogin.dto.response.AdminResponse;
import longrun.springsecuritytokenlogin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class adminController {

    private final AdminService adminService;

    @GetMapping("/admin")
    public ResponseEntity<AdminResponse> getAllUsers (){
        return ResponseEntity.ok(adminService.getAllUsers());
    }
}
