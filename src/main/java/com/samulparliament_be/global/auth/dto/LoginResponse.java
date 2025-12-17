package com.samulparliament_be.global.auth.dto;

import com.samulparliament_be.domain.users.dto.AuthProvider;

public record LoginResponse(
        Long userId,
        String name,
        String email,
        String profileImageUrl,
        AuthProvider provider,
        String accessToken,
        String refreshToken
) {
}
