package com.example.erp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI erpOpenAPI() {
        final String bearerKey = "bearerAuth";
        return new OpenAPI()
            .info(new Info()
                .title("ERP API")
                .description("API REST para el sistema ERP")
                .version("v1"))
            .components(new Components()
                .addSecuritySchemes(bearerKey, new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .addSecurityItem(new SecurityRequirement().addList(bearerKey));
    }
}
