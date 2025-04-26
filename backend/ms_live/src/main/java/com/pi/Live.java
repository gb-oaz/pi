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
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}