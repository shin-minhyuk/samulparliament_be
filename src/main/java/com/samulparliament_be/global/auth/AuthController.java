package com.samulparliament_be.global.auth;

import com.samulparliament_be.domain.users.dto.AuthProvider;
import com.samulparliament_be.global.auth.details.UserDetailsImpl;
import com.samulparliament_be.global.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/{provider}")
    public LoginResponse oauthLogin(@PathVariable String provider, @RequestParam("code") String code) {

        return authService.oauthLogin(provider, code);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.logout(userDetails.getUser().getId());
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
