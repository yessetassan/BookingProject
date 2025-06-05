package com.yesset.booking.endpoint;

import com.yesset.booking.dto.response.ResponseAnswer;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.yesset.booking.dto.user.SignUpRequest;
import com.yesset.booking.dto.user.SignInRequest;
import com.yesset.booking.dto.user.JwtAuthenticationResponse;
import com.yesset.booking.service.impl.AuthenticationService;

@RestController
@RequestMapping("/open-api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    public ResponseAnswer<Object> signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public ResponseAnswer<JwtAuthenticationResponse> signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @PostMapping("/forgot-password")
    public ResponseAnswer<Object> forgotPassword(
            @RequestParam(name = "email", required = true) String email
    ) {
        return authenticationService.forgotPassword(email);
    }
}