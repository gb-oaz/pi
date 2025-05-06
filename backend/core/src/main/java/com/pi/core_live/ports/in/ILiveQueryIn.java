package com.pi.core_live.ports.in;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.utils.constants.Router;
import com.pi.utils.constants.Request;
import com.pi.utils.exceptions.GlobalException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This interface represents the input port for the live service, which is
 * responsible for providing the live class data to the client.
 */
@Tag(name = "Live Query: port In")
public interface ILiveQueryIn {

    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Retrieve live class
            Use this endpoint to retrieve the live class
            - The queryType must be QUERY_GET_LIVE.
            - The token must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/get/live/QUERY_GET_LIVE/<key-live>",
            }
            ```
            **Example Path Params JSON:**
            ```json
            {
                "keyLive": "<key-live>",
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
                              "currentPosition": 1
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
                    @ApiResponse(responseCode = "200", description = "Live get successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @GetMapping(path = Router.ROUTER_GET_LIVE + "/{queryType}" + "/{keyLive}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_ANONYMOUS', 'SCOPE_TEACHER', 'SCOPE_STUDENT')")
    Mono<ResponseEntity<Live>> getLive(
            @PathVariable(Request.QUERY_TYPE) String queryType,
            @PathVariable(Request.KEY_LIVE) String keyLive,
            @RequestHeader(Request.AUTHORIZATION) String authorization
    ) throws GlobalException;

    @Operation(
            description = """
            ### Retrieve live class stream
            Use this endpoint to retrieve the live class
            - The queryType must be QUERY_GET_LIVE_STREAM.
            - The token must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                path: "/live/v1/get/live/stream/QUERY_GET_LIVE_STREAM/<key-live>"
            }
            ```
            **Example Path Params JSON:**
            ```json
            {
                "keyLive": "<key-live>",
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
                              "currentPosition": 1
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
                    @ApiResponse(responseCode = "200", description = "Stream de atualizações", content = { @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE, schema = @Schema(implementation = Live.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class)) })
            }
    )
    @GetMapping(path = Router.ROUTER_GET_LIVE_STREAM + "/{queryType}" + "/{keyLive}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Live> getLiveStream(
            @PathVariable(Request.QUERY_TYPE) String queryType,
            @PathVariable(Request.KEY_LIVE) String keyLive
    ) throws GlobalException;
}
