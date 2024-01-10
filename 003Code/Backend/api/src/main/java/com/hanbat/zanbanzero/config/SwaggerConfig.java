package com.hanbat.zanbanzero.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("식재료 절약단 API 명세서")
                        .description("""
                                1. 권한은 user와 manager로 구분됩니다.
                                2. /api/user는 user 권한, /api/manager는 manager 권한이 필요합니다.
                                3. manager는 user의 api를 사용할 수 있습니다.
                                4. Swagger를 통한 테스트는 '테스트 컨트롤러'에서 토큰을 발급받아 우측 Authorize 버튼을 통해 등록하여 진행할 수 있습니다.
                                (테스트 유저 계정 : user1 / 관리자는 입력 불필요)
                                5. 500번대 에러는 공통적으로 서버 에러입니다.
                                """)
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("Token",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER).name("Authorization")))
                .security(Collections.singletonList(new SecurityRequirement().addList("Token")));
    }
}
