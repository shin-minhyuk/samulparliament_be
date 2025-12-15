package com.samulparliament_be.global.auth;

import com.samulparliament_be.domain.users.entity.RefreshToken;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.domain.users.repository.RefreshTokenRepository;
import com.samulparliament_be.global.auth.dto.LoginResponse;
import com.samulparliament_be.global.auth.provider.JwtTokenProvider;
import com.samulparliament_be.global.oauth.KakaoOAuthClient;
import com.samulparliament_be.global.oauth.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login/kakao")
    public LoginResponse kakaoLogin(@RequestParam String code) {

        KakaoUserInfo userInfo = kakaoOAuthClient.getUserInfo(code);

        return authService.login(
                userInfo.email(),
                userInfo.name()
        );
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        User user = tokenEntity.getUser();

        String newAccessToken = jwtTokenProvider.createAccessToken(user);

        return new LoginResponse(
                user.getId(),
                newAccessToken,
                refreshToken
        );
    }
}
