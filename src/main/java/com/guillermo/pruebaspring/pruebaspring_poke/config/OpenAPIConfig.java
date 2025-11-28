package com.guillermo.pruebaspring.pruebaspring_poke.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pokemon Filter API")
                        .version("1.0.0")
                        .description("API REST para filtrar y buscar pokemon utilizando la PokéAPI como fuente de datos. " +
                                "Permite filtrar pokemon por tipo elemental, defensa mínima, rango de peso y experiencia base.")
                        .contact(new Contact()
                                .name("Guillermo")
                                .email("guillermo@example.com"))
                        .license(new License()
                                .name("MIT")));
    }
}
