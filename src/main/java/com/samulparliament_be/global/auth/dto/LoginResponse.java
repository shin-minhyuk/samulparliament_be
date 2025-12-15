package com.samulparliament_be.global.auth.dto;

public record LoginResponse(
        Long userId,
        String accessToken,
        String refreshToken
) {
}
