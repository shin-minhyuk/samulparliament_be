package com.samulparliament_be.global.oauth.client;

import com.samulparliament_be.domain.users.dto.AuthProvider;
import com.samulparliament_be.global.oauth.dto.OAuthUserInfo;

/**
 * 각 OAuth 제공자(Kakao, Google 등)의 공통 규격
 */
public interface OAuthClient {

    /**
     * 어떤 OAuth 제공자인지 반환
     * (KAKAO, GOOGLE 등)
     */
    AuthProvider provider();

    /**
     * OAuth 인가 코드(code)를 받아
     * 우리 서비스에서 필요한 사용자 정보로 변환
     */
    OAuthUserInfo getUserInfo(String code);
}