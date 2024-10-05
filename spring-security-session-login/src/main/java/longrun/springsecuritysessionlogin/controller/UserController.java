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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) {
        // valid오류시 오류 메세지 담아서 응답
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return ResponseEntity.badRequest().body(error.getDefaultMessage());
            }
        }
        try {
            userService.signUp(request);
        }
        catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("signup success");
    }

    @PostMapping("/forgot-id")
    public ResponseEntity<String> createIdRecoveryCode(@RequestBody ForgotIdRequest request){
        recoveryService.saveVerificationCode(request);
        return ResponseEntity.ok("create recoveryCode");
    }

    @GetMapping("/find-id-verify")
    public ResponseEntity<String> sendRecoveryCode(@RequestParam String email){
        try {
            return ResponseEntity.ok(recoveryService.validateVerificationCode(email).getId());
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
