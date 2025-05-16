package com.pi;

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
public class Mono {
    private static final Logger LOG = LoggerFactory.getLogger(Mono.class);

    @Value("${microservice.mono.routes.info.title}") String title;
    @Value("${microservice.mono.routes.info.version}") String version;
    @Value("${microservice.mono.routes.info.description}") String description;
    @Value("${microservice.mono.routes.info.contact.email}") String email;

    public static void main(String[] args) {
        SpringApplication.run(Mono.class, args);
        LOG.info("MONO SERVICE - RUNNING");
    }

    @Bean
    public GroupedOpenApi infoAuthResources() {
        String[] paths = { "/auth/v1/**" };
        var info = new Info()
                .title("1 - MS Auth v1")
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
                .group("1 - MS Auth v1")
                .addOpenApiCustomizer(openApi -> openApi.info(info))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi infoUserResources() {
        String[] paths = { "/user/v1/**" };
        var info = new Info()
                .title("2 - MS User v1")
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
    public GroupedOpenApi infoQuizResources() {
        String[] paths = { "/quiz/v1/**" };
        var info = new Info()
                .title("3 - MS Quiz v1")
                .version("1.0.0")
                .description("""
                        # 🧩 Fluxo de Utilização da API de Quiz
                        
                        ## 1. Criar um Novo Quiz
                        
                        **Endpoint:**
                        
                        `POST /quiz/v1/post/new/quiz/COMMAND_POST_NEW_QUIZ`
                        
                        **Parâmetros:**
                        
                        - `categories`: conjunto de strings representando as categorias.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Body:**
                        
                        - `login`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `code`: mínimo 6 dígitos numéricos.
                        
                        - `name`: entre 3 e 40 caracteres.
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "key": "<quiz-key-id>"
                        }
                        ```
                        
                        ---
                        
                        ## 2. Adicionar Item ao Quiz
                        
                        **Endpoint:**
                        `POST /quiz/v1/post/quiz/{key}/item/{position}/COMMAND_POST_QUIZ_ITEM`
                        
                        **Headers:**
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: application/json
                        ```
                        
                        **Body:**
                        Dependendo do tipo de item (`quizType` ou `slideType`), o corpo da requisição varia. Exemplos incluem:
                        
                        - **QUIZ_MULTIPLE_CHOICE**
                        ```json
                        {
                          "contentQuestion": "Quais desses são planetas do sistema solar?",
                          "alternatives": ["Marte", "Plutão", "Sirius", "Júpiter"],
                          "answers": ["Marte", "Júpiter"],
                          "timerSeconds": 40,
                          "reward": 25
                        }
                        ```
                        
                        **Resposta:**
                        ```json
                        {
                          "key": "<quiz-key-id>"
                        }
                        ```
                        
                        ---
                        
                        ## 3. Atualizar Item do Quiz
                        
                        **Endpoint:**
                        `PATCH /quiz/v1/patch/quiz/{key}/item/{position}/COMMAND_PATCH_QUIZ_ITEM`
                        
                        **Headers:**
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: application/json
                        ```
                        
                        **Body:**
                        Mesmo formato do item a ser atualizado.
                        
                        **Resposta:**
                        ```json
                        {
                          "key": "<quiz-key-id>"
                        }
                        ```
                        
                        ---
                        
                        ## 4. Atualizar Quiz
                        
                        **Endpoint:**
                        `PUT /quiz/v1/put/quiz/{key}/COMMAND_PUT_QUIZ`
                        
                        **Parâmetros:**
                        
                        - `categories`: conjunto de strings representando as categorias.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Body:**
                        
                        - `name`: novo nome do quiz.
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "key": "<quiz-key-id>"
                        }
                        ```
                        
                        ---
                        
                        ## 5. Remover Item do Quiz
                        
                        **Endpoint:**
                        
                        `DELETE /quiz/v1/delete/quiz/{key}/item/{position}/COMMAND_DELETE_QUIZ_ITEM`
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "key": "<quiz-key-id>"
                        }
                        ```
                        
                        ---
                        
                        ## 6. Remover Quiz
                        
                        **Endpoint:**
                        
                        `DELETE /quiz/v1/delete/quiz/{key}/COMMAND_DELETE_QUIZ`
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "key": "<quiz-key-id>"
                        }
                        ```
                        
                        ---
                        
                        ## 7. Consultar Quiz
                        
                        **Endpoint:**
                        
                        `GET /quiz/v1/get/quiz/{key}/QUERY_GET_QUIZ`
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <token>
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "key": "f7a4c1d2e3",
                          "name": "Quiz de Inovação",
                          "categories": ["tecnologia", "transformação"],
                          "quizes": [...]
                        }
                        ```
                        
                        ---
                        
                        ## 8. Consultar Projeção de Quizzes
                        
                        **Endpoint:**
                        
                        `GET /quiz/v1/get/quizes/projection/QUERY_GET_QUIZES_PROJECTION`
                        
                        **Headers:**
                        ```
                        Authorization: Bearer <token>
                        ```
                        
                        **Params:**
                        
                        ```
                        login=GUSTAVOBOAZ
                        code=123456
                        name=Quiz de Inovação
                        categories=tecnologia,transformação
                        page=1
                        size=10
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "content": [...],
                          "pageable": {
                            "page": 1,
                            "size": 10,
                            "total": 1
                          }
                        }
                        ```
                        
                        ---
                        
                        ## 9. Consultar Item do Quiz
                        
                        **Endpoint:**
                        
                        `GET /quiz/v1/get/quiz/{key}/item/{position}/QUERY_GET_QUIZ_ITEM`
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        ```
                        
                        **Resposta:**
                        
                        ```json
                        {
                          "position": 2,
                          "contentQuestion": "A transformação digital exige mudanças culturais?",
                          "answers": [true],
                          "timerSeconds": 20,
                          "reward": 50,
                          "type": "QUIZ_TRUE_FALSE"
                        }
                        ```
                        """)
                .contact(new Contact().email(email));

        return GroupedOpenApi.builder()
                .group("3 - MS Quiz v1")
                .addOpenApiCustomizer(openApi -> openApi.info(info))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi infoLiveResources() {
        String[] paths = { "/live/v1/**" };
        var info = new Info()
                .title("4 - MS Live v1")
                .version("1.0.0")
                .description("""
                        # 📺 Fluxo de Utilização da API de Live
                        
                        ## 1. Criar um Novo Live
                        
                        **Endpoint:**
                        
                        `POST /live/v1/post/new/live/COMMAND_POST_NEW_LIVE`
                        
                        **Parâmetros:**
                        
                        - `categories`: conjunto de strings representando as categorias.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Body:**
                        
                        - `login`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `code`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `keyQuiz`: chave do Quiz.
                        
                        **Example Response Body JSON:**
                        ```json
                            {
                                  "key": "Live-ABC123-login#code",
                                  "startedOn": "2025-04-27T14:30:00Z",
                                  "updateOn": "2025-04-27T14:45:00Z",
                                  "completedOn": null,
                                  "status": "PENDING",
                                  "engagement": {
                                        "participantCount": 2,
                                        "answersCorrect": 5,
                                        "answersIncorrect": 1,
                                        "answersUnanswered": 0,
                                        "correctPercentual": 83,
                                        "incorrectPercentual": 17,
                                        "unansweredPercentual": 0
                                  },
                                  "evaluation": {
                                        "evaluation": {
                                              "user1#code": [
                                                    { "position": 1, "answer": ["A"], "hit": true },
                                                    { "position": 2, "answer": ["B"], "hit": false }
                                              ],
                                              "user2#code": [
                                                    { "position": 1, "answer": ["A"], "hit": true }
                                              ]
                                        }
                                  },
                                  "quiz": {
                                        "key": "quizKey123",
                                        "login": "login",
                                        "code": "code",
                                        "name": "Sample Quiz",
                                        "quizes": [],
                                        "categories": ["math", "science"]
                                  },
                                  "teacher": {
                                        "login": "login",
                                        "code": "code",
                                        "control": {
                                          "currentPosition": 0
                                        }
                                  },
                                  "lobby": [
                                        "user1#code",
                                        "user2#code"
                                  ]
                            }
                        ```
                        
                        ## 2. Trocar de Posição
                        
                        **Endpoint:**
                        
                        `PATCH /live/v1/patch/next/position/COMMAND_PATCH_NEXT_POSITION`
                        
                        **Part Parâmetros:**
                        
                        - `login`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `code`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `keyLive`: chave do Live.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Response Body:**
                        
                        - Mesmo response Live do item 1.
                        
                        ## 3. Voltar de posição
                        
                        **Endpoint:**
                        
                        `PATCH /live/v1/patch/previous/position/COMMAND_PATCH_PREVIOUS_POSITION`
                        
                        **Part Parâmetros:**
                        
                        - `login`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `code`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `keyLive`: chave do Live.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Response Body:**
                        
                        - Mesmo response Live do item 1.
                        
                        ## 4. Remover um Pupil da live.
                        
                        **Endpoint:**
                        
                        `PATCH /live/v1/patch/remove/pupil/from/lobby/COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY`
                        
                        **Part Parâmetros:**
                        
                        - `login`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `code`: mínimo 8 caracteres em letras maiúsculas.
                        
                        - `keyLive`: chave do Live.
                        
                        - `pupilLogin`: login do Pupil.
                        
                        - `pupilCode`: code do Pupil.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Response Body:**
                        
                        - Mesmo response Live do item 1.
                        
                        ## 5. Adicionar um Pupil na live.
                        
                        **Endpoint:**
                        
                        `PATCH /live/v1/patch/add/pupil/to/lobby/COMMAND_PATCH_ADD_PUPIL_TO_LOBBY`
                        
                        **Part Parâmetros:**
                        
                        - `keyLive`: chave do Live.
                        
                        - `pupilLogin`: login do Pupil.
                        
                        - `pupilCode`: code do Pupil.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Response Body:**
                        
                        - Mesmo response Live do item 1.

                        ## 6. Adicionar uma resposta a uma pergunta.
                        
                        **Endpoint:**
                        
                        `PATCH /live/v1/patch/add/pupil/answer/to/quiz/COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ`
                        
                        **Query Parâmetros:**
                        
                        - `answerItem`: item da resposta.
                        
                        **Part Parâmetros:**
                        
                        - `keyLive`: chave do Live.
                        
                        - `pupilLogin`: login do Pupil.
                        
                        - `pupilCode`: code do Pupil.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Response Body:**
                        
                        - Mesmo response Live do item 1.
                        
                        ## 7. Finalizar a Live.
                        
                        **Endpoint:**
                        
                        `PATCH /live/v1/patch/end/live/COMMAND_PATCH_END_LIVE`
                        
                        **Part Parâmetros:**
                        
                        - `keyLive`: chave do Live.
                        
                        - `login`: login do Professor.
                        
                        - `code`: code do Professor.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <teacher-token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Response Body:**
                        
                        - Mesmo response Live do item 1.
                        
                        ## 8. Pegar Live por chave.
                        
                        **Endpoint:**
                        
                        `GET /live/v1/get/live/QUERY_GET_LIVE/<key-live>`
                        
                        **Part Parâmetros:**
                        
                        - `key-live`: chave do Live.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <token>
                        Content-Type: multipart/form-data
                        ```
                        
                        **Response Body:**
                        
                        - Mesmo response Live do item 1.
                        
                        ## 9. Pegar Live stream por chave.
                        
                        **Endpoint:**
                        
                        `GET /live/v1/get/live/stream/QUERY_GET_LIVE_STREAM/<key-live>`
                        
                        **Part Parâmetros:**
                        
                        - `key-live`: chave do Live.
                        
                        **Headers:**
                        
                        ```
                        Authorization: Bearer <token>
                        Content-Type: text/event-stream
                        ```
                        
                        **Response Body:**
                        
                        - Mesmo response Live do item 1.
                        
                        """)
                .contact(new Contact().email(email));

        return GroupedOpenApi.builder()
                .group("4 - MS Live v1")
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