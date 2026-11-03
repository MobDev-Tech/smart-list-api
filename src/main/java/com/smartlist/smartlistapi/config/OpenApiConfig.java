package com.smartlist.smartlistapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Smart List API",
        version = "v1",
        description = "API para gerenciamento de lista de compras, configurações, residência e notificações."
    ),
    servers = @Server(url = "http://localhost:8080", description = "Servidor local")
)
public class OpenApiConfig {
}
