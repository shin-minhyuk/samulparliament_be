package com.samulparliament_be.global.oauth.dto;

import com.samulparliament_be.domain.users.dto.AuthProvider;

public record KakaoUserInfo(
        String email,
        String name,
        AuthProvider provider
) {
}
