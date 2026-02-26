package com.veris.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Veris Backend API")
                                                .version("1.0.0")
                                                .description("API REST para gestión de pacientes - Madrid Gestión Médica")
                                                .contact(new Contact()
                                                                .name("Veris Support")
                                                                .email("support@veris.com")))
                                // Añadiendo los esquemas al UI (Disponible en Autorización global)
                                .addSecurityItem(new SecurityRequirement().addList("basicAuth").addList("bearerAuth"))
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes("basicAuth",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("basic")
                                                                                .description("Basic Authentication para Login"))
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .description("JWT Bearer token")));
        }
}
