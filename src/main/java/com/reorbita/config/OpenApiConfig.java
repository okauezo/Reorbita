package com.reorbita.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reorbitaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("REORBITA - Plataforma de Inteligencia Orbital")
                .version("1.0.0")
                .description("API de manutencao orbital preditiva da REORBITA."));
    }
}
