package com.samulparliament_be.global.auth;

import com.samulparliament_be.domain.users.dto.AuthProvider;
import com.samulparliament_be.domain.users.entity.RefreshToken;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.domain.users.repository.RefreshTokenRepository;
import com.samulparliament_be.domain.users.repository.UserRepository;
import com.samulparliament_be.global.auth.dto.LoginResponse;
import com.samulparliament_be.global.auth.provider.JwtTokenProvider;
import com.samulparliament_be.global.exception.BusinessException;
import com.samulparliament_be.global.exception.ErrorCode;
import com.samulparliament_be.global.oauth.client.OAuthClient;
import com.samulparliament_be.global.oauth.dto.OAuthUserInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final List<OAuthClient> oauthClients;
    private Map<AuthProvider, OAuthClient> oauthClientMap;

    @PostConstruct
    public void init() {
        oauthClientMap = oauthClients.stream()
                .collect(Collectors.toMap(
                        OAuthClient::provider,
                        client -> client
                ));
    }

    public LoginResponse oauthLogin(String provider, String code) {
        log.info("[AUTH] OAuth 로그인 시도 | provider={}", provider);

        AuthProvider authProvider;
        
        try {
            authProvider = AuthProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        }

        // 1. provider에 맞는 OAuthClient 선택
        OAuthClient client = oauthClientMap.get(authProvider);

        // 2. OAuth 서버에서 사용자 정보 조회
        OAuthUserInfo userInfo = client.getUserInfo(code);
        log.info("[AUTH] OAuth 사용자 정보 조회 성공 | email={}", userInfo.email());
        
        // 3. 사용자 조회 or 생성
        boolean isNewUser = userRepository.findByEmailAndProvider(userInfo.email(), authProvider).isEmpty();
        
        User user = userRepository
                .findByEmailAndProvider(userInfo.email(), authProvider)
                .orElseGet(() ->
                        userRepository.save(
                                User.create(
                                        userInfo.email(),
                                        userInfo.name(),
                                        userInfo.profileImageUrl(),
                                        authProvider
                                )
                        )
                );
        
        if (isNewUser) {
            log.info("[AUTH] 신규 사용자 생성 | userId={} email={}", user.getId(), user.getEmail());
        }
        
        // 프로필 이미지가 변경된 경우 업데이트
        if (!Objects.equals(userInfo.profileImageUrl(), user.getProfileImageUrl())) {
            user.updateProfileImageUrl(userInfo.profileImageUrl());
        }

        // 4. JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        // 5. RefreshToken 저장/갱신
        saveOrUpdateRefreshToken(user, refreshToken);

        log.info("[AUTH] 로그인 성공 | userId={} provider={}", user.getId(), provider);
        
        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getProvider(),
                accessToken,
                refreshToken
        );
    }

    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
        log.info("[AUTH] 로그아웃 | userId={}", userId);
    }

    public LoginResponse refresh(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));

        if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }

        User user = tokenEntity.getUser();
        String newAccessToken = jwtTokenProvider.createAccessToken(user);

        log.info("[AUTH] 토큰 갱신 | userId={}", user.getId());
        
        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getProvider(),
                newAccessToken,
                refreshToken
        );
    }

    private void saveOrUpdateRefreshToken(User user, String refreshToken) {
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(14);

        RefreshToken tokenEntity =
                refreshTokenRepository.findByUser(user)
                        .map(existing -> {
                            existing.update(refreshToken, expiresAt);
                            return existing;
                        })
                        .orElse(
                                RefreshToken.create(user, refreshToken, 14)
                        );

        refreshTokenRepository.save(tokenEntity);
    }
}

