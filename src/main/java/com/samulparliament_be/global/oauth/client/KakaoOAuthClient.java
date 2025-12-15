package com.samulparliament_be.global.oauth.client;

import com.samulparliament_be.domain.users.dto.AuthProvider;
import com.samulparliament_be.global.oauth.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient {

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.token-uri}")
    private String tokenUri;

    @Value("${oauth.kakao.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AuthProvider provider() {
        return AuthProvider.KAKAO;
    }

    @Override
    public OAuthUserInfo getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        return getUserProfile(accessToken);
    }


    /**
     * 1. code → access_token
     */
    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                tokenUri,
                request,
                Map.class
        );

        System.out.println((String) response.getBody().get("access_token"));

        return (String) response.getBody().get("access_token");
    }

    /**
     * 2. access_token → 사용자 정보
     */
    private OAuthUserInfo getUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();

        Map<String, Object> kakaoAccount =
                (Map<String, Object>) body.get("kakao_account");

        Map<String, Object> profile =
                (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String name = (String) profile.get("nickname");

        return new OAuthUserInfo(email, name);
    }
}
