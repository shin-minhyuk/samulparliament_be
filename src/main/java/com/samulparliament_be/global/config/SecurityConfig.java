package com.samulparliament_be.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 끄기 (REST API)
                .csrf(csrf -> csrf.disable())

                // form login 끄기 (HTML 로그인 페이지 제거)
                .formLogin(form -> form.disable())

                // http basic 끄기
                .httpBasic(basic -> basic.disable())

                // 지금은 전부 허용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
