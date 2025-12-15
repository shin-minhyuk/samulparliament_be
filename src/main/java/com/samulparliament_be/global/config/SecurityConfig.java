package com.samulparliament_be.global.config;

import com.samulparliament_be.global.auth.provider.JwtAuthenticationFilter;
import com.samulparliament_be.global.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("JWT 기반 인증만 사용합니다.");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. CSRF 비활성화 (JWT는 Stateless)
                .csrf(csrf -> csrf.disable())

                // 기본 로그인 폼 / Basic 인증 비활성화
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 2. 세션 사용 안 함 (JWT 기반)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. URL 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 허용 (로그인 / 문서)
                        .requestMatchers(
                                "/swagger-ui/**",      // Swagger UI 화면
                                "/v3/api-docs/**",     // Swagger JSON 문서
                                "/api/auth/**"         // 로그인 / 토큰 발급 API
                        ).permitAll()

                        // 그 외 모든 요청은 JWT 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                // → 요청마다 AccessToken 검사 후 SecurityContext에 사용자 저장
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}