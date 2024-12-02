package longrun.springsecuritysessionlogin.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.SignUpRequest;
import longrun.springsecuritysessionlogin.dto.request.ForgotIdRequest;
import longrun.springsecuritysessionlogin.dto.response.ForgotIdResponse;
import longrun.springsecuritysessionlogin.dto.response.LoginResponse;
import longrun.springsecuritysessionlogin.dto.response.SignUpResponse;
import longrun.springsecuritysessionlogin.dto.response.SuccessCode;
import longrun.springsecuritysessionlogin.service.RecoveryService;
import longrun.springsecuritysessionlogin.service.UserService;
import org.springframework.http.HttpStatus;
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

    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        User user = userService.signUp(request);
        SuccessCode successCode = SuccessCode.USER_REGISTERED;
        SignUpResponse response = SignUpResponse.builder()
                .status(successCode.getStatus())
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .value(user.getUserId()).build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-id")
    public ResponseEntity<ForgotIdResponse> createIdRecoveryCode(@RequestBody ForgotIdRequest request) {
        userService.findByEmail(request.getEmail());
        recoveryService.saveVerificationCode(request);
        SuccessCode successCode = SuccessCode.CREATE_VERIFICATION_CODE;
        ForgotIdResponse response = ForgotIdResponse.builder()
                .status(successCode.getStatus())
                .code(successCode.getCode())
                .message(successCode.getMessage()).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-id-verify")
    public ResponseEntity<ForgotIdResponse> sendRecoveryCode(@RequestParam("email") String email, @RequestParam("verificationCode") String verificationCode) {
        SuccessCode successCode = SuccessCode.VALID_VERIFICATION_CODE;
        ForgotIdResponse response = ForgotIdResponse.builder()
                .status(successCode.getStatus())
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .value(recoveryService.validateVerificationCode(email, verificationCode)).build();
        return ResponseEntity.ok(response);
    }
}
