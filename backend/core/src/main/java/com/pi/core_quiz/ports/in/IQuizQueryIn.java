package com.pi.core_quiz.ports.in;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.utils.constants.Router;
import com.pi.utils.constants.Query;
import com.pi.utils.constants.Request;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * Interface for Quiz Query In Port
 * This interface defines the methods for querying quizzes.
 */
@Tag(name = "Quiz Query: port In")
public interface IQuizQueryIn {

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Get Quiz
            Use this endpoint to get a Quiz.
            - The queryType must be QUERY_GET_QUIZ.
            - The token must be a valid token.
            - key is the unique identifier of the quiz.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/get/quiz/{key}/QUERY_GET_QUIZ"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <token>",
            }
            ```
            
            **Example Response Body JSON:**
            ```json
                    {
                          "key": "f7a4c1d2e3",
                          "login": "GUSTAVOBOAZ",
                          "code": "123456",
                          "name": "Quiz de Inovação",
                          "categories": ["tecnologia", "transformação", "estratégia"],
                          "quizes": [
                                {
                                      "position": 1,
                                      "contentQuestion": "Qual é o principal benefício da transformação digital?",
                                      "quantityCharacters": 100,
                                      "answers": ["Agilidade", "Automação", "Inovação"],
                                      "timerSeconds": 30,
                                      "type": "QUIZ_WORD_CLOUD"
                                },
                                {
                                      "position": 2,
                                      "contentQuestion": "A transformação digital exige mudanças culturais?",
                                      "answers": [true],
                                      "timerSeconds": 20,
                                      "reward": 50,
                                      "type": "QUIZ_TRUE_FALSE"
                                }
                          ]
                    }
            ```
            
            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Quiz.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @GetMapping(path = Router.ROUTER_GET_QUIZ + "/{queryType}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER', 'SCOPE_ANONYMOUS', 'SCOPE_STUDENT')")
    ResponseEntity<Quiz> getQuiz(
            @PathVariable(Query.KEY) String key,
            @PathVariable(Request.QUERY_TYPE) String queryType,
            @RequestHeader(Request.AUTHORIZATION) String token
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Get Quizes
            Use this endpoint to get a Quizes projection.
            - The queryType must be QUERY_GET_QUIZES_PROJECTION.
            - The token must be a valid token.
            - The login, code, name, categories must be provided in the request params.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/get/quizes/projection/QUERY_GET_QUIZES_PROJECTION"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <token>",
            }
            ```
            
            **Example Request Params JSON:**
            ```json
            {
                "login": "GUSTAVOBOAZ",
                "code": "123456",
                "name": "Quiz de Inovação",
                "categories": ["tecnologia", "transformação"],
                "page": 1,
                "size": 10
            }
            ```
            
            **Example Response Body JSON:**
            ```json
                {
                    "content": [
                        {
                              "key": "f7a4c1d2e3",
                              "login": "GUSTAVOBOAZ",
                              "code": "123456",
                              "name": "Quiz de Inovação",
                              "categories": ["tecnologia", "transformação", "estratégia"],
                              "quizes": [
                                    {
                                          "position": 1,
                                          "contentQuestion": "Qual é o principal benefício da transformação digital?",
                                          "quantityCharacters": 100,
                                          "answers": ["Agilidade", "Automação", "Inovação"],
                                          "timerSeconds": 30,
                                          "type": "QUIZ_WORD_CLOUD"
                                    },
                                    {
                                          "position": 2,
                                          "contentQuestion": "A transformação digital exige mudanças culturais?",
                                          "answers": [true],
                                          "timerSeconds": 20,
                                          "reward": 50,
                                          "type": "QUIZ_TRUE_FALSE"
                                    }
                              ]
                        }
                    ],
                    "pageable": {
                        "page": 1,
                        "size": 10,
                        "total": 1
                    }
                }
            ```
            
            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Pageable.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @GetMapping(path = Router.ROUTER_GET_QUIZES_PROJECTION + "/{queryType}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER', 'SCOPE_ANONYMOUS', 'SCOPE_STUDENT')")
    ResponseEntity<Pageable<Quiz>> getQuizesProjection(
            @PathVariable(Request.QUERY_TYPE) String queryType,
            @RequestHeader(Request.AUTHORIZATION) String token,
            @RequestParam(value = Query.LOGIN, required = false) String login,
            @RequestParam(value = Query.CODE, required = false) String code,
            @RequestParam(value = Query.NAME, required = false) String name,
            @RequestParam(value = Query.CATEGORIES, required = false) Set<String> categories,
            @RequestParam(name = Request.PAGE, required = false, defaultValue = "1") Integer page,
            @RequestParam(name = Request.SIZE, required = false, defaultValue = "10") Integer size
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Get item Quiz
            Use this endpoint to get a Quiz.
            - The queryType must be QUERY_GET_QUIZ_ITEM.
            - The token must be a valid token.
            - key is the unique identifier of the quiz.
            - position is the unique identifier of the quiz item.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/get/quiz/{key}/item/{position}/QUERY_GET_QUIZ_ITEM"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <teacher-token>",
            }
            ```
            
            **Example Response Body JSON:**
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
            
            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = IQuizItem.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @GetMapping(path = Router.ROUTER_GET_QUIZ_ITEM + "/{queryType}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    ResponseEntity<IQuizItem> getQuizItem(
            @PathVariable(Query.KEY) String key,
            @PathVariable(Query.POSITION) Integer position,
            @PathVariable(Request.QUERY_TYPE) String queryType,
            @RequestHeader(Request.AUTHORIZATION) String teacherToken
    ) throws GlobalException;
}
