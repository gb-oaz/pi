package com.pi;

import com.pi.core_user.utils.constants.Router;

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
        String[] paths = { Router.SERVER_INFO };
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