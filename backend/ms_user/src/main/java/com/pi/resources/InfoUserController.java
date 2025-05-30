package com.pi.resources;

import com.pi.core_user.core.utils.constants.Router;
import com.pi.utils.models.GlobalInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Info")
@RestController
public class InfoUserController {
    private static final Logger LOG = LoggerFactory.getLogger(InfoUserController.class);

    @Value("${microservice.user.name}") String name;
    @Value("${microservice.user.version}") String version;
    @Value("${microservice.user.description}") String description;

    @Operation(
            description = "Provide custom server info", responses = {
            @ApiResponse(responseCode = "200", description = "retrieve global info api", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalInfo.class))
            }),
    })
    @GetMapping(path = Router.ROUTER_USER_INFO)
    public ResponseEntity<GlobalInfo> getInfo() {
        var result = GlobalInfo.builder().name(name).version(version).description(description).build();

        LOG.debug(result.toString());
        return ResponseEntity.ok(result);
    }
}
