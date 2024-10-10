package longrun.springsecuritytokenlogin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import longrun.springsecuritytokenlogin.dto.request.EmailAuthenticateCodeRequest;
import longrun.springsecuritytokenlogin.dto.request.SignupRequest;
import longrun.springsecuritytokenlogin.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request){
        userService.signUp(request);
        return ResponseEntity.ok("signup success");
    }

    @GetMapping("/find-id")
    public ResponseEntity<String> sendAuthenticateCode(@Valid @RequestBody EmailAuthenticateCodeRequest emailAuthenticateCodeRequest){
        return ResponseEntity.ok(userService.sendAuthenticateCode(emailAuthenticateCodeRequest));
    }

    @GetMapping("find-id-verify")
    public ResponseEntity<String> verifyAuthenticateCode(@Valid @RequestBody EmailAuthenticateCodeRequest emailAuthenticateCodeRequest){
        return ResponseEntity.ok(userService.verifyAuthenticateCode(emailAuthenticateCodeRequest));
    }
}