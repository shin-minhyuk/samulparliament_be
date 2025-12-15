package com.samulparliament_be.global.auth;

import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.domain.users.repository.UserRepository;
import com.samulparliament_be.global.auth.dto.LoginResponse;
import com.samulparliament_be.global.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(String email, String name) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.create(email, name)));

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        return new LoginResponse(
                user.getId(),
                accessToken,
                refreshToken
        );
    }
}
