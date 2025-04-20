package com.pi.utils.security.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import static com.pi.core_auth.core.utils.constants.Router.ROUTER_AUTH_INFO;
import static com.pi.core_auth.core.utils.constants.Router.ROUTER_POST_ANONYMOUS_TOKEN;
import static com.pi.core_auth.core.utils.constants.Router.ROUTER_POST_SIGN_IN_TOKEN;
import static com.pi.core_auth.core.utils.constants.Router.ROUTER_GET_STATUS_TOKEN;
import static com.pi.core_auth.core.utils.constants.Router.ROUTER_GET_SCOPE_TOKEN;
import static com.pi.core_auth.core.enums.CommandType.COMMAND_POST_ANONYMOUS_TOKEN;
import static com.pi.core_auth.core.enums.CommandType.COMMAND_POST_SIGN_IN_TOKEN;
import static com.pi.core_auth.core.enums.QueryType.QUERY_GET_STATUS_TOKEN;
import static com.pi.core_auth.core.enums.QueryType.QUERY_GET_SCOPE_TOKEN;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> {
                    // SWAGGER - REDDOCs
                    authorize.requestMatchers(HttpMethod.GET, "/docs/**").permitAll();
                    // MS_AUTH
                    authorize.requestMatchers(HttpMethod.GET, ROUTER_AUTH_INFO).permitAll();
                    authorize.requestMatchers(HttpMethod.POST, ROUTER_POST_ANONYMOUS_TOKEN + "/" + COMMAND_POST_ANONYMOUS_TOKEN.name()).permitAll();

                    // MS_USER
                    authorize.requestMatchers(HttpMethod.GET, ROUTER_USER_INFO).permitAll();

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
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
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
}