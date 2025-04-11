package com.db.crud_pessoas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Gerenciamento de Pessoas")
                .version("1.0")
                .description("API para cadastro e gerenciamento de pessoas e seus endereços")
                .contact(new Contact()
                    .name("Lucas Miranda")
                    .email("ldemattosmiranda@gmail.com")));
    }
}
