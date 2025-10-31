package com.waduclay.multiauthentication.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;


/**
 * Configuration class for JWT (JSON Web Token) encoding and decoding.
 * This class provides the necessary beans for JWT operations in the application,
 * including the JWT encoder and decoder configured with the application's secret key.
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Configuration
public class JwtConfiguration {
    private static final Logger log = LoggerFactory.getLogger(JwtConfiguration.class);
    @Value("${jwt.secret}")
    private String jwtSecret;
    private SecretKeySpec secretKey;


    /**
     * Initialises the secret key used for JWT operations.
     * This method is called after the dependency injection is complete.
     */
    @PostConstruct
    void init() {
        log.debug("Initializing JWT secret key");
        secretKey = new SecretKeySpec(jwtSecret.getBytes(), JwsAlgorithms.HS256);
        log.debug("JWT secret key initialized successfully");
    }

    /**
     * Creates and configures a JWT decoder bean.
     * This decoder is used to validate and decode JWT tokens in the application.
     *
     * @return A configured JwtDecoder instance
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        log.debug("Creating JWT decoder bean");
        JwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        log.debug("JWT decoder bean created successfully");
        return decoder;
    }

    /**
     * Creates and configures a JWT encoder bean.
     * This encoder is used to create JWT tokens in the application.
     *
     * @return A configured JwtEncoder instance
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        log.debug("Creating JWT encoder bean");
        JwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
        log.debug("JWT encoder bean created successfully");
        return encoder;
    }
}
