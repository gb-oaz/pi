package com.pi.core_live.ports.in;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.utils.constants.Router;
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
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

import java.util.List;

/**
 * This interface represents the input port for the live service, which is
 * responsible for exposing the commands to the client.
 */
@Tag(name = "Live Commands: port In")
public interface ILiveCommandIn {

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Create live class
            Use this endpoint to create the live class
            - The commandType must be COMMAND_POST_NEW_LIVE.
            - The token must be a valid token.
            - The keyQuiz must be a valid key.
            - The teacher login and code must be a valid login and code.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/post/new/live/COMMAND_POST_NEW_LIVE"
            }
            ```
            
            **Example Part params JSON:**
            ```json
            {
                "login": "<teacher-login>",
                "code": "<teacher-code>",
                "keyQuiz": "<key-quiz>"
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

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Live created successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PostMapping(path = Router.ROUTER_POST_NEW_LIVE + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    Mono<ResponseEntity<Live>> postNewLive(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String authorization,
            @RequestPart(Command.LOGIN) String login,
            @RequestPart(Command.CODE) String code,
            @RequestPart(Request.KEY_QUIZ) String keyQuiz
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Next position control live class
            Use this endpoint to move the teacher to the next position
            - The commandType must be COMMAND_PATCH_NEXT_POSITION.
            - The token must be a valid token.
            - The keyLive must be a valid key.
            - The teacher login and code must be a valid login and code.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/patch/next/position/COMMAND_PATCH_NEXT_POSITION"
            }
            ```
            
            **Example Part params JSON:**
            ```json
            {
                "login": "<teacher-login>",
                "code": "<teacher-code>",
                "keyLive": "<key-live>"
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

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Live patch successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PatchMapping(path = Router.ROUTER_PATCH_NEXT_POSITION + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    Mono<ResponseEntity<Live>> patchNextPosition(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String authorization,
            @RequestPart(Command.LOGIN) String login,
            @RequestPart(Command.CODE) String code,
            @RequestPart(Request.KEY_LIVE) String keyLive
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Previous position control live class
            Use this endpoint to move the teacher to the next position
            - The commandType must be COMMAND_PATCH_PREVIOUS_POSITION.
            - The token must be a valid token.
            - The keyLive must be a valid key.
            - The teacher login and code must be a valid login and code.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/patch/previous/position/COMMAND_PATCH_PREVIOUS_POSITION"
            }
            ```
            
            **Example Part params JSON:**
            ```json
            {
                "login": "<teacher-login>",
                "code": "<teacher-code>",
                "keyLive": "<key-live>"
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

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Live patch successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PatchMapping(path = Router.ROUTER_PATCH_PREVIOUS_POSITION + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    Mono<ResponseEntity<Live>> patchPreviousPosition(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String authorization,
            @RequestPart(Command.LOGIN) String login,
            @RequestPart(Command.CODE) String code,
            @RequestPart(Request.KEY_LIVE) String keyLive
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Remove pupil live class
            Use this endpoint to remove the pupil from the live class
            - The commandType must be COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY.
            - The token must be a valid token.
            - The keyLive must be a valid key.
            - The teacher login and code must be a valid login and code.
            - The pupil login and code must be a valid login and code.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/patch/remove/pupil/from/lobby/COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY"
            }
            ```
            
            **Example Part params JSON:**
            ```json
            {
                "login": "<teacher-login>",
                "code": "<teacher-code>",
                "keyLive": "<key-live>"
                "pupilLogin": "<pupil-login>",
                "pupilCode": "<pupil-code>"
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

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Live patch successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PatchMapping(path = Router.ROUTER_PATCH_REMOVE_PUPIL_FROM_LOBBY + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    Mono<ResponseEntity<Live>> patchRemovePupilFromLobby(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String authorization,
            @RequestPart(Command.LOGIN) String login,
            @RequestPart(Command.CODE) String code,
            @RequestPart(Request.KEY_LIVE) String keyLive,
            @RequestPart(Request.PUPIL_LOGIN) String pupilLogin,
            @RequestPart(Request.PUPIL_CODE) String pupilCode
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Add pupil live class
            Use this endpoint to add the pupil from the live class
            - The commandType must be COMMAND_PATCH_ADD_PUPIL_TO_LOBBY.
            - The token must be a valid token.
            - The keyLive must be a valid key.
            - The pupil login and code must be a valid login and code.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/patch/add/pupil/to/lobby/COMMAND_PATCH_ADD_PUPIL_TO_LOBBY"
            }
            ```
            
            **Example Part params JSON:**
            ```json
            {
                "keyLive": "<key-live>"
                "pupilLogin": "<pupil-login>",
                "pupilCode": "<pupil-code>"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <your-token>",
            }
            ```
            
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

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Live patch successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PatchMapping(path = Router.ROUTER_PATCH_ADD_PUPIL_TO_LOBBY + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_ANONYMOUS', 'SCOPE_TEACHER', 'SCOPE_STUDENT')")
    Mono<ResponseEntity<Live>> patchAddPupilToLobby(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String authorization,
            @RequestPart(Request.KEY_LIVE) String keyLive,
            @RequestPart(Request.PUPIL_LOGIN) String pupilLogin,
            @RequestPart(Request.PUPIL_CODE) String pupilCode
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Add answer to pupil live class
            Use this endpoint to add the pupil from the live class
            - The commandType must be COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ.
            - The token must be a valid token.
            - The keyLive must be a valid key.
            - The pupil login and code must be a valid login and code.
            - The answer must be a valid answer.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/patch/add/pupil/answer/to/quiz/COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ"
            }
            ```
            
            **Example Query params JSON:**
            ```json
            {
                "answerItem": ["<item>", "<item>"]
            }
            ```
            
            **Example Part params JSON:**
            ```json
            {
                "keyLive": "<key-live>"
                "pupilLogin": "<pupil-login>",
                "pupilCode": "<pupil-code>"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <your-token>",
            }
            ```
            
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

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Live patch successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PatchMapping(path = Router.ROUTER_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_ANONYMOUS', 'SCOPE_TEACHER', 'SCOPE_STUDENT')")
    Mono<ResponseEntity<Live>> patchAddPupilAnswerToQuiz(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String authorization,
            @RequestParam(Request.ANSWER_ITEM) List<String> answerItem,
            @RequestPart(Request.KEY_LIVE) String keyLive,
            @RequestPart(Request.PUPIL_LOGIN) String pupilLogin,
            @RequestPart(Request.PUPIL_CODE) String pupilCode
    ) throws GlobalException;

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### End live class
            Use this endpoint to add the pupil from the live class
            - The commandType must be COMMAND_PATCH_END_LIVE.
            - The token must be a valid token.
            - The keyLive must be a valid key.
            - The login and code must be a valid login and code.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/patch/end/live/COMMAND_PATCH_END_LIVE"
            }
            ```
            
            **Example Part params JSON:**
            ```json
            {
                "keyLive": "<key-live>"
                "login": "<login>",
                "code": "<code>"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <your-token>",
            }
            ```
            
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

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Live patch successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @PatchMapping(path = Router.ROUTER_PATCH_END_LIVE + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER')")
    Mono<ResponseEntity<Live>> patchEndLive(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String authorization,
            @RequestPart(Request.KEY_LIVE) String keyLive,
            @RequestPart(Request.LOGIN) String login,
            @RequestPart(Request.CODE) String code
    ) throws GlobalException;
}
