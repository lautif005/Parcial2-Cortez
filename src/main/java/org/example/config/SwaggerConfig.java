package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API")
                        .version("1.0.0")
                        .description("API REST para detectar mutantes analizando secuencias de ADN. Implementa optimizaciones como Early Termination y Caché con Hash SHA-256.") //
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu.email@ejemplo.com"))
                );
    }
}
