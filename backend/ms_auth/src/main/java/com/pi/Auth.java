package com.pi;

import com.pi.core_auth.core.utils.constants.Router;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Auth {
    private static final Logger LOG = LoggerFactory.getLogger(Auth.class);

    @Value("${microservice.auth.routes.info.title}") String title;
    @Value("${microservice.auth.routes.info.version}") String version;
    @Value("${microservice.auth.routes.info.description}") String description;
    @Value("${microservice.auth.routes.info.contact.email}") String email;

    public static void main(String[] args) {
        SpringApplication.run(Auth.class, args);
        LOG.info("AUTH MICROSERVICE - RUNNING");
    }

    @Bean
    public GroupedOpenApi infoAuth() {
        String[] paths = { Router.ROUTER_AUTH_INFO};
        var info = new Info().title(title).version(version).description(description).contact(new Contact().email(email));

        return GroupedOpenApi.builder()
                .group("1 - Info")
                .addOpenApiCustomizer(openApi -> openApi.info(info))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi infoAuthResources() {
        String[] paths = { "/auth/v1/**" };
        var info = new Info()
                .title("2 - MS Auth v1")
                .version("1.0.0")
                .description("""
                        # 🔐 Fluxo de Utilização da API de Autenticação
                        
                        ## 1. Obter Token Anônimo
                        
                        **Endpoint:**
                        
                        `POST /auth/v1/post/anonymous/token/COMMAND_POST_ANONYMOUS_TOKEN`
                        
                        **Uso:**
                        
                        Utilizado como primeiro passo para interações. Gera um token anônimo para ser usado nas próximas requisições.
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "token": "Bearer <anonymous-token>",
                          "createAt": "2024-09-23T10:00:00Z",
                          "expiryAt": "2024-09-24T10:00:00Z",
                          "status": "ACTIVE"
                        }
                        ```
                        
                        ---
                        
                        ## 2. Login e Geração de Token de Acesso
                        
                        **Endpoint:**
                        
                        `POST /auth/v1/post/sign/in/token/COMMAND_POST_SIGN_IN_TOKEN`
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <anonymous-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Body:**
                        
                        - `login`: mínimo 8 caracteres, letras maiúsculas.
                        
                        - `code`: mínimo 6 dígitos numéricos.
                        
                        - `password`: mínimo 15 caracteres, com letras maiúsculas, minúsculas, números e especiais.
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "token": "Bearer <access-token>",
                          "createAt": "2024-09-23T10:00:00Z",
                          "expiryAt": "2024-09-24T10:00:00Z",
                          "status": "ACTIVE"
                        }
                        ```
                        
                        ---
                        
                        ## 3. Validar Status do Token
                        
                        **Endpoint:**
                        
                        `GET /auth/v1/get/status/token/QUERY_GET_STATUS_TOKEN`
                        
                        **Headers:**
                        ```
                        Authorization: Bearer <access-token>
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "status": "ACTIVE"
                        }
                        ```
                        
                        ---
                        
                        ## 4. Validar Scope do Token
                        
                        **Endpoint:**
                        
                        `GET /auth/v1/get/scope/token/QUERY_GET_SCOPE_TOKEN`
                        
                        **Headers:**
                        ```
                        Authorization: Bearer <access-token>
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "scope": ["SCOPE_ANONYMOUS", "SCOPE_TEACHER", "SCOPE_STUDENT"],
                          "login": "GUSTAVOBOAZ",
                          "code": "123456"
                        }
                        ```
                        """)
                .contact(new Contact().email(email));

        return GroupedOpenApi.builder()
                .group("2 - MS Auth v1")
                .addOpenApiCustomizer(openApi -> openApi.info(info))
                .pathsToMatch(paths)
                .build();
    }


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}