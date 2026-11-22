package com.smartlist.smartlistapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "ApiKeyAuth",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = ApiKeyFilter.API_KEY_HEADER
)
@OpenAPIDefinition(
    info = @Info(
        title = "Smart List API",
        version = "v1",
        description = "API para gerenciamento de lista de compras, configurações, residência e notificações."
    ),
    servers = @Server(url = "/", description = "Servidor atual")
)
public class OpenApiConfig {
}
