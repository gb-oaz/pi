package com.pi.core_quiz.ports.in;

import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.utils.constants.Router;
import com.pi.core_quiz.core.utils.models.Response;
import com.pi.utils.constants.Command;
import com.pi.utils.constants.Request;
import com.pi.utils.exceptions.GlobalException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.Set;

/**
 * Quiz Command Port In
 * This interface defines the methods for creating, updating, deleting, and modifying quiz items.
 */
@Tag(name = "Quiz Commands: port In")
public interface IQuizCommandIn {

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Create new Quiz
            Use this endpoint to create a new Quiz.
            - The commandType must be COMMAND_POST_NEW_QUIZ.
            - The login must be at least 8 characters uppercase letters.
            - The code must be at least 6 numbers.
            - The name of quiz must be between 3 and 40 characters with space.
            - The categories must be a set of strings.
            - The teacher-Token must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/post/new/quiz/COMMAND_POST_NEW_QUIZ?categories=Java&categories=Programação&categories=Lógica"
            }
            ```
            
            **Example Request Params JSON:**
            ```json
            {
                "categories": ["Java", "Programação", "Lógica"]
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <teacher-token>",
            }
            ```

            **Example Request Body Parts JSON:**
            ```json
            {
                "login": "GUSTAVOBOAZ",
                "code": "123456",
                "name": "Quiz de Programação Java"
            }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "key": "<quiz-key-id>"
            }
            ```
            
            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PostMapping(path = Router.ROUTER_POST_NEW_QUIZ + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    ResponseEntity<Response> postNewQuiz(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String teacherToken,
            @RequestPart(Command.LOGIN) String login,
            @RequestPart(Command.CODE) String code,
            @RequestPart(Command.NAME) String name,
            @RequestParam(value = Command.CATEGORIES, required = false) Set<String> categories
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Add new quiz item in existing Quiz
            Use this endpoint to add a new quiz item to an existing Quiz.
            - The commandType must be COMMAND_POST_QUIZ_ITEM.
            - The teacher-Token must be a valid token.
            - The typeItem one value, quizType or slideType:
                - quizType:
                    - QUIZ_MULTIPLE_CHOICE
                    - QUIZ_FILL_SPACE
                    - QUIZ_TRUE_FALSE
                    - QUIZ_OPEN
                    - QUIZ_POLL
                    - QUIZ_WORD_CLOUD
                - slideType:
                    - SLIDE_TITLE_1
                    - SLIDE_TITLE_2
                    - SLIDE_TEXT_1
                    - SLIDE_TEXT_2
                    - SLIDE_TEXT_MEDIA_1
                    - SLIDE_TEXT_MEDIA_2
            - The quizItem must be a valid quiz item.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/post/quiz/{key}/item/{position}/COMMAND_POST_QUIZ_ITEM"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <teacher-token>",
            }
            ```

            **Example Request Body quizType JSON:**
            
            ***QUIZ_MULTIPLE_CHOICE***
            ```json
                {
                    "type": "QUIZ_MULTIPLE_CHOICE",
                    "contentQuestion": "Quais desses são planetas do sistema solar?",
                    "alternatives": ["Marte", "Plutão", "Sirius", "Júpiter"],
                    "answers": ["Marte", "Júpiter"],
                    "timerSeconds": 40,
                    "reward": 25
                }
            ```
            
            ***QUIZ_FILL_SPACE***
            ```json
                {
                    "type": "QUIZ_FILL_SPACE",
                    "contentQuestion": "Complete: A água ferve a ___ graus Celsius.",
                    "answers": ["100"],
                    "timerSeconds": 25,
                    "reward": 15
                }
            ```
            
            ***QUIZ_TRUE_FALSE***
            ```json
                {
                    "type": "QUIZ_TRUE_FALSE",
                    "contentQuestion": "O céu é azul durante o dia?",
                    "answers": [true],
                    "timerSeconds": 20,
                    "reward": 10
                }
            ```
            
            ***QUIZ_OPEN***
            ```json
                {
                    "type": "QUIZ_OPEN",
                    "contentQuestion": "Explique o que significa inovação para você.",
                    "quantityCharacters": 200,
                    "answers": ["Criar algo novo", "Melhorar processos", "Pensar diferente"],
                    "timerSeconds": 90,
                    "reward": 50
                }
            ```
            
            ***QUIZ_POLL***
            ```json
                {
                    "type": "QUIZ_POLL",
                    "contentQuestion": "Qual sua cor favorita?",
                    "answers": ["Azul", "Verde", "Vermelho", "Amarelo"],
                    "timerSeconds": 30
                }
            ```
            
            ***QUIZ_WORD_CLOUD***
            ```json
                {
                    "type": "QUIZ_WORD_CLOUD",
                    "contentQuestion": "Quais são as suas palavras favoritas?",
                    "quantityCharacters": 20,
                    "answers": ["Liberdade", "Conhecimento", "Coragem"],
                    "timerSeconds": 60
                }
            ```
            
            **Example Request Body slideType JSON:**
            
            ***SLIDE_TITLE_1***
            ```json
                {
                    "type": "SLIDE_TITLE_1",
                    "contentTitle": "Início da Apresentação"
                }
            ```
            
            ***SLIDE_TITLE_2***
            ```json
                {
                    "type": "SLIDE_TITLE_2",
                    "contentTitle": "Bem-vindo à Jornada do Conhecimento",
                    "contentDescription": "Prepare-se para explorar ideias, responder perguntas e acumular recompensas!"
                }
            ```
            
            ***SLIDE_TEXT_1***
            ```json
                {
                    "type": "SLIDE_TEXT_1",
                    "contentHeader": "Entendendo o Contexto",
                    "contentSubHeaderOne": "Por que isso é importante?",
                    "contentTextOne": "Neste módulo, vamos abordar os fundamentos que sustentam a nossa estratégia de inovação."
                }
            ```
            
            ***SLIDE_TEXT_2***
            ```json
                {
                    "type": "SLIDE_TEXT_2",
                    "contentHeader": "Pilares da Transformação",
                    "contentSubHeaderOne": "Tecnologia como Aliada",
                    "contentTextOne": "Utilizamos ferramentas digitais para otimizar processos e ampliar resultados.",
                    "contentSubHeaderTwo": "Pessoas no Centro",
                    "contentTextTwo": "Capacitamos nossos times para serem protagonistas da mudança."
                }
            ```
            
            ***SLIDE_TEXT_MEDIA_1***
            ```json
                {
                    "type": "SLIDE_TEXT_MEDIA_1",
                    "contentHeader": "Explorando a Mídia",
                    "contentTextOne": "A inovação tecnológica não é apenas sobre ferramentas, mas sobre como as usamos para transformar a experiência.",
                    "contentMediaOne": "https://link.da.midia.exemplo/video.mp4"
                }
            ```
            
            ***SLIDE_TEXT_MEDIA_2***
            ```json
                {
                    "type": "SLIDE_TEXT_MEDIA_2",
                    "contentSubHeaderOne": "Primeira perspective",
                    "contentTextOne": "A mudança é inevitável, mas a forma como nos adaptamos a ela define nosso sucesso.",
                    "contentMediaOne": "https://link.da.midia.exemplo/primeiro_video.mp4",
                    "contentSubHeaderTwo": "Segunda perspective",
                    "contentTextTwo": "A colaboração entre equipes é a chave para transformar desafios em oportunidades.",
                    "contentMediaTwo": "https://link.da.midia.exemplo/segundo_video.mp4"
                }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "key": "<quiz-key-id>"
            }
            ```

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PostMapping(path = Router.ROUTER_POST_QUIZ_ITEM + "/{commandType}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    ResponseEntity<Response> postQuizItem(
            @PathVariable(Command.KEY) String key,
            @PathVariable(Command.POSITION) Integer position,
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestParam(Command.TYPE_ITEM) String typeItem,
            @RequestHeader(Request.AUTHORIZATION) String teacherToken,
            @RequestBody IQuizItem quizItem
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Change item in existing Quiz
            Use this endpoint whe update quiz item to an existing Quiz.
            - The commandType must be COMMAND_PATCH_QUIZ_ITEM.
            - The teacher-Token must be a valid token.
            - The typeItem one value, quizType or slideType:
                - quizType:
                    - QUIZ_MULTIPLE_CHOICE
                    - QUIZ_FILL_SPACE
                    - QUIZ_TRUE_FALSE
                    - QUIZ_OPEN
                    - QUIZ_POLL
                    - QUIZ_WORD_CLOUD
                - slideType:
                    - SLIDE_TITLE_1
                    - SLIDE_TITLE_2
                    - SLIDE_TEXT_1
                    - SLIDE_TEXT_2
                    - SLIDE_TEXT_MEDIA_1
                    - SLIDE_TEXT_MEDIA_2
            - The quizItem must be a valid quiz item.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/patch/quiz/{key}/item/{position}/COMMAND_PATCH_QUIZ_ITEM"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <teacher-token>",
            }
            ```

            **Example Request Body quizType JSON:**
            
            ***QUIZ_MULTIPLE_CHOICE***
            ```json
                {
                    "type": "QUIZ_MULTIPLE_CHOICE",
                    "contentQuestion": "Quais desses são planetas do sistema solar?",
                    "alternatives": ["Marte", "Plutão", "Sirius", "Júpiter"],
                    "answers": ["Marte", "Júpiter"],
                    "timerSeconds": 40,
                    "reward": 25
                }
            ```
            
            ***QUIZ_FILL_SPACE***
            ```json
                {
                    "type": "QUIZ_FILL_SPACE",
                    "contentQuestion": "Complete: A água ferve a ___ graus Celsius.",
                    "answers": ["100"],
                    "timerSeconds": 25,
                    "reward": 15
                }
            ```
            
            ***QUIZ_TRUE_FALSE***
            ```json
                {
                    "type": "QUIZ_TRUE_FALSE",
                    "contentQuestion": "O céu é azul durante o dia?",
                    "answers": [true],
                    "timerSeconds": 20,
                    "reward": 10
                }
            ```
            
            ***QUIZ_OPEN***
            ```json
                {
                    "type": "QUIZ_OPEN",
                    "contentQuestion": "Explique o que significa inovação para você.",
                    "quantityCharacters": 200,
                    "answers": ["Criar algo novo", "Melhorar processos", "Pensar diferente"],
                    "timerSeconds": 90,
                    "reward": 50
                }
            ```
            
            ***QUIZ_POLL***
            ```json
                {
                    "type": "QUIZ_POLL",
                    "contentQuestion": "Qual sua cor favorita?",
                    "answers": ["Azul", "Verde", "Vermelho", "Amarelo"],
                    "timerSeconds": 30
                }
            ```
            
            ***QUIZ_WORD_CLOUD***
            ```json
                {
                    "type": "QUIZ_WORD_CLOUD",
                    "contentQuestion": "Quais são as suas palavras favoritas?",
                    "quantityCharacters": 20,
                    "answers": ["Liberdade", "Conhecimento", "Coragem"],
                    "timerSeconds": 60
                }
            ```
            
            **Example Request Body slideType JSON:**
            
            ***SLIDE_TITLE_1***
            ```json
                {
                    "type": "SLIDE_TITLE_1",
                    "contentTitle": "Início da Apresentação"
                }
            ```
            
            ***SLIDE_TITLE_2***
            ```json
                {
                    "type": "SLIDE_TITLE_2",
                    "contentTitle": "Bem-vindo à Jornada do Conhecimento",
                    "contentDescription": "Prepare-se para explorar ideias, responder perguntas e acumular recompensas!"
                }
            ```
            
            ***SLIDE_TEXT_1***
            ```json
                {
                    "type": "SLIDE_TEXT_1",
                    "contentHeader": "Entendendo o Contexto",
                    "contentSubHeaderOne": "Por que isso é importante?",
                    "contentTextOne": "Neste módulo, vamos abordar os fundamentos que sustentam a nossa estratégia de inovação."
                }
            ```
            
            ***SLIDE_TEXT_2***
            ```json
                {
                    "type": "SLIDE_TEXT_2",
                    "contentHeader": "Pilares da Transformação",
                    "contentSubHeaderOne": "Tecnologia como Aliada",
                    "contentTextOne": "Utilizamos ferramentas digitais para otimizar processos e ampliar resultados.",
                    "contentSubHeaderTwo": "Pessoas no Centro",
                    "contentTextTwo": "Capacitamos nossos times para serem protagonistas da mudança."
                }
            ```
            
            ***SLIDE_TEXT_MEDIA_1***
            ```json
                {
                    "type": "SLIDE_TEXT_MEDIA_1",
                    "contentHeader": "Explorando a Mídia",
                    "contentTextOne": "A inovação tecnológica não é apenas sobre ferramentas, mas sobre como as usamos para transformar a experiência.",
                    "contentMediaOne": "https://link.da.midia.exemplo/video.mp4"
                }
            ```
            
            ***SLIDE_TEXT_MEDIA_2***
            ```json
                {
                    "type": "SLIDE_TEXT_MEDIA_2",
                    "contentSubHeaderOne": "Primeira perspective",
                    "contentTextOne": "A mudança é inevitável, mas a forma como nos adaptamos a ela define nosso sucesso.",
                    "contentMediaOne": "https://link.da.midia.exemplo/primeiro_video.mp4",
                    "contentSubHeaderTwo": "Segunda perspective",
                    "contentTextTwo": "A colaboração entre equipes é a chave para transformar desafios em oportunidades.",
                    "contentMediaTwo": "https://link.da.midia.exemplo/segundo_video.mp4"
                }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "key": "<quiz-key-id>"
            }
            ```

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PatchMapping(path = Router.ROUTER_PATCH_QUIZ_ITEM + "/{commandType}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    ResponseEntity<Response> patchQuizItem(
            @PathVariable(Command.KEY) String key,
            @PathVariable(Command.POSITION) Integer position,
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestParam(Command.TYPE_ITEM) String typeItem,
            @RequestHeader(Request.AUTHORIZATION) String teacherToken,
            @RequestBody IQuizItem quizItem
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Update an existing Quiz
            Use this endpoint to update Quiz.
            - The commandType must be COMMAND_PUT_QUIZ.
            - The name of quiz must be between 3 and 40 characters with space.
            - The categories must be a set of strings.
            - The teacher-Token must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/put/quiz/{key}/COMMAND_PUT_QUIZ?categories=Java&categories=Programação&categories=Lógica"
            }
            ```
            
            **Example Request Params JSON:**
            ```json
            {
                "categories": ["Java", "Programação", "Lógica"]
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <teacher-token>",
            }
            ```

            **Example Request Body Parts JSON:**
            ```json
            {
                "name": "Quiz de Programação Java"
            }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "key": "<quiz-key-id>"
            }
            ```

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PutMapping(path = Router.ROUTER_PUT_QUIZ + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    ResponseEntity<Response> putQuiz(
            @PathVariable(Command.KEY) String key,
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String teacherToken,
            @RequestPart(value = Command.NAME, required = false) String name,
            @RequestParam(value = Command.CATEGORIES, required = false) Set<String> categories
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Delete an existing item in Quiz
            Use this endpoint to delete item in Quiz.
            - The commandType must be COMMAND_DELETE_QUIZ_ITEM.
            - The teacher-Token must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/delete/quiz/{key}/item/{position}/COMMAND_DELETE_QUIZ_ITEM"
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
                "key": "<quiz-key-id>"
            }
            ```

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @DeleteMapping(path = Router.ROUTER_DELETE_QUIZ_ITEM + "/{commandType}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    ResponseEntity<Response> deleteQuizItem(
            @PathVariable(Command.KEY) String key,
            @PathVariable(Command.POSITION) Integer position,
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String teacherToken
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Delete an existing Quiz
            Use this endpoint to delete Quiz.
            - The commandType must be COMMAND_DELETE_QUIZ.
            - The teacher-Token must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                path: "/quiz/v1/delete/quiz/{key}/COMMAND_DELETE_QUIZ"
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
                "key": "<quiz-key-id>"
            }
            ```

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz generated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @DeleteMapping(path = Router.ROUTER_DELETE_QUIZ + "/{commandType}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    ResponseEntity<Response> deleteQuiz(
            @PathVariable(Command.KEY) String key,
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String teacherToken
    ) throws GlobalException;
}
