package com.pi.utils.security.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.pi.core_auth.core.utils.constants.Router.ROUTER_AUTH_INFO;
import static com.pi.core_auth.core.utils.constants.Router.ROUTER_POST_ANONYMOUS_TOKEN;
import static com.pi.core_auth.core.utils.constants.Router.ROUTER_POST_SIGN_IN_TOKEN;
import static com.pi.core_auth.core.utils.constants.Router.ROUTER_GET_STATUS_TOKEN;
import static com.pi.core_auth.core.utils.constants.Router.ROUTER_GET_SCOPE_TOKEN;
import static com.pi.core_auth.core.enums.CommandType.COMMAND_POST_ANONYMOUS_TOKEN;
import static com.pi.core_auth.core.enums.CommandType.COMMAND_POST_SIGN_IN_TOKEN;
import static com.pi.core_auth.core.enums.QueryType.QUERY_GET_STATUS_TOKEN;
import static com.pi.core_auth.core.enums.QueryType.QUERY_GET_SCOPE_TOKEN;

import static com.pi.core_live.core.enums.CommandType.COMMAND_POST_NEW_LIVE;
import static com.pi.core_live.core.enums.CommandType.COMMAND_PATCH_NEXT_POSITION;
import static com.pi.core_live.core.enums.CommandType.COMMAND_PATCH_PREVIOUS_POSITION;
import static com.pi.core_live.core.enums.CommandType.COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY;
import static com.pi.core_live.core.enums.CommandType.COMMAND_PATCH_ADD_PUPIL_TO_LOBBY;
import static com.pi.core_live.core.enums.CommandType.COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ;
import static com.pi.core_live.core.enums.CommandType.COMMAND_PATCH_END_LIVE;
import static com.pi.core_live.core.enums.QueryType.QUERY_GET_LIVE;

import static com.pi.core_live.core.enums.QueryType.QUERY_GET_LIVE_STREAM;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_GET_LIVE;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_GET_LIVE_STREAM;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_LIVE_INFO;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_PATCH_ADD_PUPIL_TO_LOBBY;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_PATCH_END_LIVE;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_PATCH_NEXT_POSITION;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_PATCH_PREVIOUS_POSITION;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_PATCH_REMOVE_PUPIL_FROM_LOBBY;
import static com.pi.core_live.core.utils.constants.Router.ROUTER_POST_NEW_LIVE;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_QUIZ_INFO;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_GET_QUIZ;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_GET_QUIZES_PROJECTION;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_GET_QUIZ_ITEM;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_PATCH_QUIZ_ITEM;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_POST_NEW_QUIZ;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_POST_QUIZ_ITEM;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_PUT_QUIZ;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_DELETE_QUIZ;
import static com.pi.core_quiz.core.utils.constants.Router.ROUTER_DELETE_QUIZ_ITEM;
import static com.pi.core_quiz.core.enums.CommandType.COMMAND_PATCH_QUIZ_ITEM;
import static com.pi.core_quiz.core.enums.CommandType.COMMAND_POST_NEW_QUIZ;
import static com.pi.core_quiz.core.enums.CommandType.COMMAND_POST_QUIZ_ITEM;
import static com.pi.core_quiz.core.enums.CommandType.COMMAND_PUT_QUIZ;
import static com.pi.core_quiz.core.enums.CommandType.COMMAND_DELETE_QUIZ;
import static com.pi.core_quiz.core.enums.CommandType.COMMAND_DELETE_QUIZ_ITEM;
import static com.pi.core_quiz.core.enums.QueryType.QUERY_GET_QUIZ;
import static com.pi.core_quiz.core.enums.QueryType.QUERY_GET_QUIZES_PROJECTION;
import static com.pi.core_quiz.core.enums.QueryType.QUERY_GET_QUIZ_ITEM;

import static com.pi.core_user.core.utils.constants.Router.ROUTER_USER_INFO;
import static com.pi.core_user.core.utils.constants.Router.ROUTER_GET_USER_BY_PROJECTION;
import static com.pi.core_user.core.utils.constants.Router.ROUTER_GET_USERS_BY_PROJECTION;
import static com.pi.core_user.core.utils.constants.Router.ROUTER_POST_CREATE_USER_TEACHER;
import static com.pi.core_user.core.utils.constants.Router.ROUTER_POST_CREATE_USER_STUDENT;
import static com.pi.core_user.core.utils.constants.Router.ROUTER_PUT_UPDATE_USER;
import static com.pi.core_user.core.enums.CommandType.COMMAND_POST_CREATE_USER_TEACHER;
import static com.pi.core_user.core.enums.CommandType.COMMAND_POST_CREATE_USER_STUDENT;
import static com.pi.core_user.core.enums.CommandType.COMMAND_PUT_UPDATE_USER;
import static com.pi.core_user.core.enums.QueryType.QUERY_GET_USERS_BY_PROJECTION;
import static com.pi.core_user.core.enums.QueryType.QUERY_GET_USER_BY_PROJECTION;

@Configuration
@EnableMethodSecurity
public class GlobalSecurityConfig {

    @Value("${jwt.public.key}") RSAPublicKey publicKey;
    @Value("${jwt.private.key}") RSAPrivateKey privateKey;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomAuthenticationEntryPoint authEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        http
                .authorizeHttpRequests(authorize -> {
                    // SWAGGER - REDDOCs
                    authorize.requestMatchers(HttpMethod.GET, "/docs/**").permitAll();
                    // MS_AUTH
                    authorize.requestMatchers(HttpMethod.GET, ROUTER_AUTH_INFO).permitAll();
                    authorize.requestMatchers(HttpMethod.POST, ROUTER_POST_ANONYMOUS_TOKEN + "/" + COMMAND_POST_ANONYMOUS_TOKEN.name()).permitAll();

                    // MS_USER
                    authorize.requestMatchers(HttpMethod.GET, ROUTER_USER_INFO).permitAll();

                    // MS_QUIZ
                    authorize.requestMatchers(HttpMethod.GET, ROUTER_QUIZ_INFO).permitAll();

                    // MS_LIVE
                    authorize.requestMatchers(HttpMethod.GET, ROUTER_LIVE_INFO).permitAll();

                    // OTHER REQUESTS
                    authorize.anyRequest().authenticated();
                })
                .csrf(csrf -> {
                    // MS_AUTH
                    csrf.ignoringRequestMatchers(ROUTER_AUTH_INFO);
                    csrf.ignoringRequestMatchers(ROUTER_POST_ANONYMOUS_TOKEN + "/" + COMMAND_POST_ANONYMOUS_TOKEN.name());
                    csrf.ignoringRequestMatchers(ROUTER_POST_SIGN_IN_TOKEN + "/" + COMMAND_POST_SIGN_IN_TOKEN.name());
                    csrf.ignoringRequestMatchers(ROUTER_GET_STATUS_TOKEN + "/" + QUERY_GET_STATUS_TOKEN.name());
                    csrf.ignoringRequestMatchers(ROUTER_GET_SCOPE_TOKEN + "/" + QUERY_GET_SCOPE_TOKEN.name());

                    // MS_USER
                    csrf.ignoringRequestMatchers(ROUTER_USER_INFO);
                    csrf.ignoringRequestMatchers(ROUTER_GET_USER_BY_PROJECTION + "/" + QUERY_GET_USER_BY_PROJECTION.name());
                    csrf.ignoringRequestMatchers(ROUTER_GET_USERS_BY_PROJECTION + "/" + QUERY_GET_USERS_BY_PROJECTION.name());
                    csrf.ignoringRequestMatchers(ROUTER_POST_CREATE_USER_TEACHER + "/" + COMMAND_POST_CREATE_USER_TEACHER.name());
                    csrf.ignoringRequestMatchers(ROUTER_POST_CREATE_USER_STUDENT + "/" + COMMAND_POST_CREATE_USER_STUDENT.name());
                    csrf.ignoringRequestMatchers(ROUTER_PUT_UPDATE_USER + "/" + COMMAND_PUT_UPDATE_USER.name());

                    // MS_QUIZ
                    csrf.ignoringRequestMatchers(ROUTER_QUIZ_INFO);
                    csrf.ignoringRequestMatchers(ROUTER_GET_QUIZ + "/" + QUERY_GET_QUIZ.name());
                    csrf.ignoringRequestMatchers(ROUTER_GET_QUIZES_PROJECTION + "/" + QUERY_GET_QUIZES_PROJECTION.name());
                    csrf.ignoringRequestMatchers(ROUTER_GET_QUIZ_ITEM + "/" + QUERY_GET_QUIZ_ITEM.name());
                    csrf.ignoringRequestMatchers(ROUTER_POST_NEW_QUIZ + "/" + COMMAND_POST_NEW_QUIZ.name());
                    csrf.ignoringRequestMatchers(ROUTER_POST_QUIZ_ITEM + "/" + COMMAND_POST_QUIZ_ITEM.name());
                    csrf.ignoringRequestMatchers(ROUTER_PATCH_QUIZ_ITEM + "/" + COMMAND_PATCH_QUIZ_ITEM.name());
                    csrf.ignoringRequestMatchers(ROUTER_PUT_QUIZ + "/" + COMMAND_PUT_QUIZ.name());
                    csrf.ignoringRequestMatchers(ROUTER_DELETE_QUIZ_ITEM + "/" + COMMAND_DELETE_QUIZ_ITEM.name());
                    csrf.ignoringRequestMatchers(ROUTER_DELETE_QUIZ + "/" + COMMAND_DELETE_QUIZ.name());

                    // MS_LIVE
                    csrf.ignoringRequestMatchers(ROUTER_GET_LIVE + "/" + QUERY_GET_LIVE.name());
                    csrf.ignoringRequestMatchers(ROUTER_GET_LIVE_STREAM + "/" + QUERY_GET_LIVE_STREAM.name());
                    csrf.ignoringRequestMatchers(ROUTER_POST_NEW_LIVE + "/" + COMMAND_POST_NEW_LIVE.name());
                    csrf.ignoringRequestMatchers(ROUTER_PATCH_NEXT_POSITION + "/" + COMMAND_PATCH_NEXT_POSITION.name());
                    csrf.ignoringRequestMatchers(ROUTER_PATCH_PREVIOUS_POSITION + "/" + COMMAND_PATCH_PREVIOUS_POSITION.name());
                    csrf.ignoringRequestMatchers(ROUTER_PATCH_REMOVE_PUPIL_FROM_LOBBY + "/" + COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY.name());
                    csrf.ignoringRequestMatchers(ROUTER_PATCH_ADD_PUPIL_TO_LOBBY + "/" + COMMAND_PATCH_ADD_PUPIL_TO_LOBBY.name());
                    csrf.ignoringRequestMatchers(ROUTER_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ + "/" + COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ.name());
                    csrf.ignoringRequestMatchers(ROUTER_PATCH_END_LIVE + "/" + COMMAND_PATCH_END_LIVE.name());
                })
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // ou domínios específicos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}