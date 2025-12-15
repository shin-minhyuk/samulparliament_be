package com.samulparliament_be.global.auth;

import com.samulparliament_be.domain.users.entity.RefreshToken;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.domain.users.repository.RefreshTokenRepository;
import com.samulparliament_be.global.auth.details.UserDetailsImpl;
import com.samulparliament_be.global.auth.dto.LoginResponse;
import com.samulparliament_be.global.auth.provider.JwtTokenProvider;
import com.samulparliament_be.global.oauth.KakaoOAuthClient;
import com.samulparliament_be.global.oauth.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoOAuthClient kakaoOAuthClient;

    @PostMapping("/login/kakao")
    public LoginResponse kakaoLogin(@RequestParam String code) {

        KakaoUserInfo userInfo = kakaoOAuthClient.getUserInfo(code);

        return authService.login(
                userInfo.email(),
                userInfo.name(),
                userInfo.provider()
        );
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
