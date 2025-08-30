package com.roadmap.fase1.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aplicação rest JWT e fila")
                        .version("1.0")
                        .description("Documentação da aplicação fase 1 do roadmap java 🚀"))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório Git ").url("https://github.com/dudscode/rest-jwt-oauth-filas"));
    }
}
