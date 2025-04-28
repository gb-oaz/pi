package com.pi;

import com.pi.core_live.core.utils.constants.Router;

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
public class Live {
    private static final Logger LOG = LoggerFactory.getLogger(Live.class);

    @Value("${microservice.live.routes.info.title}") String title;
    @Value("${microservice.live.routes.info.version}") String version;
    @Value("${microservice.live.routes.info.description}") String description;
    @Value("${microservice.live.routes.info.contact.email}") String email;

    public static void main(String[] args) {
        SpringApplication.run(Live.class, args);
        LOG.info("LIVE MICROSERVICE - RUNNING");
    }

    @Bean
    public GroupedOpenApi infoAuth() {
        String[] paths = { Router.ROUTER_LIVE_INFO};
        var info = new Info().title(title).version(version).description(description).contact(new Contact().email(email));

        return GroupedOpenApi.builder()
                .group("1 - Info")
                .addOpenApiCustomizer(openApi -> openApi.info(info))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi infoLiveResources() {
        String[] paths = { "/live/v1/**" };
        var info = new Info()
                .title("2 - MS Live v1")
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
                        
                        """)
                .contact(new Contact().email(email));

        return GroupedOpenApi.builder()
                .group("2 - MS Live v1")
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