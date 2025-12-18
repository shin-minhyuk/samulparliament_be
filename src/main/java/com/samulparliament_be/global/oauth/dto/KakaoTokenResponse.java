package com.samulparliament_be.global.oauth.dto;

public record KakaoTokenResponse(
    String token_type,
    String access_token,
    Integer expires_in,
    String refresh_token,
    Integer refresh_token_expires_in,
    String scope
) {}