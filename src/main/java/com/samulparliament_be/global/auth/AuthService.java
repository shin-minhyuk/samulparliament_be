package com.samulparliament_be.global.auth;

import com.samulparliament_be.domain.users.entity.RefreshToken;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.domain.users.repository.RefreshTokenRepository;
import com.samulparliament_be.domain.users.repository.UserRepository;
import com.samulparliament_be.global.auth.dto.LoginResponse;
import com.samulparliament_be.global.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse login(String email, String name) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.create(email, name)));

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        return new LoginResponse(user.getId(), accessToken, refreshToken);
    }

    public LoginResponse refresh(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(tokenEntity); // 만료 토근 폐기
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        User user = tokenEntity.getUser();
        String newAccessToken = jwtTokenProvider.createAccessToken(user);

        return new LoginResponse(user.getId(), newAccessToken, refreshToken);
    }
}
