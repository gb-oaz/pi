package com.pi;

import com.pi.core_user.core.utils.constants.Router;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class User {
    private static final Logger LOG = LoggerFactory.getLogger(User.class);

    @Value("${microservice.user.routes.info.title}") String title;
    @Value("${microservice.user.routes.info.version}") String version;
    @Value("${microservice.user.routes.info.description}") String description;
    @Value("${microservice.user.routes.info.contact.email}") String email;

    public static void main(String[] args) {
        SpringApplication.run(User.class, args);
        LOG.info("USER MICROSERVICE - RUNNING");
    }

    @Bean
    public GroupedOpenApi infoAuth() {
        String[] paths = { Router.ROUTER_USER_INFO};
        var info = new Info().title(title).version(version).description(description).contact(new Contact().email(email));

        return GroupedOpenApi.builder()
                .group("1 - Info")
                .addOpenApiCustomizer(openApi -> openApi.info(info))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi infoUserResources() {
        String[] paths = { "/user/v1/**" };
        var info = new Info()
                .title("2 - MS Auth v1")
                .version("1.0.0")
                .description("""
                        # 🧑‍🏫 Fluxo de Utilização da API de Usuários
                        
                        ## 1. Criar Usuário - Perfil Professor
                        
                        **Endpoint:**
                        
                        `POST /user/v1/post/create/user/teacher/COMMAND_POST_CREATE_USER_TEACHER`
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <anonymous-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Body:**
                        
                        - `name`: entre 3 e 40 caracteres, pode conter espaços.
                        
                        - `email`: email válido.
                        
                        - `login`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `code`: mínimo 6 dígitos numéricos.
                        
                        - `password`: mínimo 15 caracteres, contendo:
                        
                          - uma letra minúscula,
                          - uma letra maiúscula,
                          - um número,
                          - um caractere especial (!?@#$%&).
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "name": "Gustavo Boaz",
                          "email": "gustavo.boaz@domainpi.com",
                          "login": "GUSTAVOBOAZ",
                          "code": "123456",
                          "createAt": "2024-09-23T10:00:00Z",
                          "updateAt": "2024-09-23T10:00:00Z"
                        }
                        ```
                        
                        ---
                        
                        ## 2. Criar Usuário - Perfil Estudante
                        
                        **Endpoint:**
                        
                        `POST /user/v1/post/create/user/student/COMMAND_POST_CREATE_USER_STUDENT`
                        
                        **Headers:**
                        ```
                        Authorization: Bearer <anonymous-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Body:**
                        
                        Mesmo formato da criação de professor.
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "name": "Gustavo Boaz",
                          "email": "gustavo.boaz@domainpi.com",
                          "login": "GUSTAVOBOAZ",
                          "code": "123456",
                          "createAt": "2024-09-23T10:00:00Z",
                          "updateAt": "2024-09-23T10:00:00Z"
                        }
                        ```
                        
                        ---
                        
                        ## 3. Atualizar Dados do Usuário
                        
                        **Endpoint:**
                        
                        `PUT /user/v1/put/update/user/COMMAND_PUT_UPDATE_USER`
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Body:**
                        
                        - `name`: entre 3 e 40 caracteres.
                        
                        - `email`: email válido.
                        
                        - `password`: nova senha seguindo os critérios de segurança.
                        
                        - `oldPassword`: senha anterior.
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "name": "Gustavo Boaz",
                          "email": "gustavo.boaz@domainpi.com",
                          "createAt": "2024-09-23T10:00:00Z",
                          "updateAt": "2024-09-23T10:00:00Z"
                        }
                        ```
                        
                        ---
                        
                        ## 4. Obter Informações do Usuário (Por Email/Login/Código)
                        
                        **Endpoint:**
                        
                        `GET /user/v1/get/user/by/projection/QUERY_GET_USER_BY_PROJECTION`
                        
                        **Headers:**
                        ```
                        Authorization: Bearer <token>
                        ```
                        
                        **Params:**
                        
                        ```
                        email=gustavo.boaz@domainpi.com \s
                        login=GUSTAVOBOAZ \s
                        code=123456
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "name": "Gustavo Boaz",
                          "email": "gustavo.boaz@domainpi.com",
                          "login": "GUSTAVOBOAZ",
                          "code": "123456",
                          "createAt": "2024-09-23T10:00:00Z",
                          "updateAt": "2024-09-23T10:00:00Z"
                        }
                        ```
                        
                        ---
                        
                        ## 5. Buscar Lista de Usuários (Com Filtro)
                        
                        **Endpoint:**
                        
                        `GET /user/v1/get/users/by/projection/QUERY_GET_USERS_BY_PROJECTION`
                        
                        **Headers:**
                        ```
                        Authorization: Bearer <token>
                        ```
                        
                        **Params:**
                        
                        ```
                        name=Gustavo Boaz \s
                        email=gustavo.boaz@domainpi.com \s
                        login=GUSTAVOBOAZ \s
                        code=123456 \s
                        page=0 \s
                        size=10
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "content": [
                            {
                              "name": "Gustavo Boaz",
                              "email": "gustavo.boaz@domainpi.com",
                              "login": "GUSTAVOBOAZ",
                              "code": "123456",
                              "createAt": "2024-09-23T10:00:00Z",
                              "updateAt": "2024-09-23T10:00:00Z"
                            }
                          ],
                          "pageable": {
                            "page": 0,
                            "size": 10,
                            "total": 1
                          }
                        }
                        ```
                        """)
                .contact(new Contact().email(email));

        return GroupedOpenApi.builder()
                .group("2 - MS User v1")
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