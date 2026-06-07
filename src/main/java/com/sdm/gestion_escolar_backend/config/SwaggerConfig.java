package com.sdm.gestion_escolar_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Sistema de Gestión Escolar IEP SDM")
                        .version("1.0.0")
                        .description("API REST para la gestión integral de estudiantes, docentes, cursos y notas académicas. " +
                                "Este sistema permite administrar el ciclo completo de evaluación académica, desde el registro " +
                                "de usuarios hasta el cálculo de promedios y estados académicos.")
                        )
                .addServersItem(new Server()
                        .url("http://localhost:8080/api")
                        .description("Servidor de Desarrollo"));
                // .addServersItem(new Server()
                //         .url("https://gestion-escolar-backend.onrender.com/api")
                //         .description("Servidor de Producción"));
    }
}
