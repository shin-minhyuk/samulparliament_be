package com.samulparliament_be.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Open Board API 문서")
                        .description("커뮤니티 게시판 서비스 API")
                        .version("1.0")
                )

                // ⭐ 핵심 1: 어떤 보안 스킴을 쓸지 명시
                .addSecurityItem(
                        new SecurityRequirement().addList("bearerAuth")
                )

                // ⭐ 핵심 2: bearerAuth 정의
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}