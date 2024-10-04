package longrun.springsecuritysessionlogin.controller;


import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.dto.request.SignupRequest;
import longrun.springsecuritysessionlogin.dto.request.ForgotIdRequest;
import longrun.springsecuritysessionlogin.dto.response.ForgotIdResponse;
import longrun.springsecuritysessionlogin.service.RecoveryService;
import longrun.springsecuritysessionlogin.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RecoveryService recoveryService;

    public static PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        userService.signUp(request);
        return ResponseEntity.ok("signup success");
    }

    @PostMapping("/forgot-id")
    public ResponseEntity<String> createIdRecoveryCode(@RequestBody ForgotIdRequest request){
        recoveryService.saveVerificationCode(request);
        return ResponseEntity.ok("create recoveryCode");
    }

    @GetMapping("/find-id-verify")
    public ResponseEntity<ForgotIdResponse> sendRecoveryCode(@RequestParam String email){
        ForgotIdResponse response = recoveryService.validateVerificationCode(email);
        return ResponseEntity.ok(response);
    }
}
