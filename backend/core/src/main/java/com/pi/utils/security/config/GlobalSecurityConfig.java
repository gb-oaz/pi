package com.pi.utils.security.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import com.pi.core_auth.core.enums.CommandType;
import com.pi.core_auth.core.enums.QueryType;
import com.pi.core_auth.core.utils.constants.Router;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
                    authorize.requestMatchers(HttpMethod.GET, com.pi.core_auth.core.utils.constants.Router.SERVER_INFO).permitAll();
                    authorize.requestMatchers(HttpMethod.POST, Router.POST_ANONYMOUS_TOKEN + "/" + CommandType.POST_ANONYMOUS_TOKEN.name()).permitAll();

                    // MS_USER
                    authorize.requestMatchers(HttpMethod.GET, com.pi.core_user.utils.constants.Router.SERVER_INFO).permitAll();
                    // OTHER REQUESTS
                    authorize.anyRequest().authenticated();
                })
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers(com.pi.core_auth.core.utils.constants.Router.SERVER_INFO);
                    csrf.ignoringRequestMatchers(com.pi.core_user.utils.constants.Router.SERVER_INFO);
                    csrf.ignoringRequestMatchers(Router.POST_ANONYMOUS_TOKEN + "/" + CommandType.POST_ANONYMOUS_TOKEN.name());
                    csrf.ignoringRequestMatchers(Router.POST_SIGN_IN_TOKEN + "/" + CommandType.POST_SIGN_IN_TOKEN.name());
                    csrf.ignoringRequestMatchers(Router.GET_STATUS_TOKEN + "/" + QueryType.GET_STATUS_TOKEN.name());
                    csrf.ignoringRequestMatchers(Router.GET_SCOPE_TOKEN + "/" + QueryType.GET_SCOPE_TOKEN.name());
                })
                .httpBasic(Customizer.withDefaults())
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