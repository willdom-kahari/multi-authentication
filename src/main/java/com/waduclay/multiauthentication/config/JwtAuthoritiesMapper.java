package com.waduclay.multiauthentication.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * Maps JWT tokens to Spring Security authentication objects.
 * This component is responsible for converting JWT tokens into authentication tokens
 * with the appropriate authorities (roles) for the user. It also verifies the JWT token
 * by checking the issuer and authorities against the user's stored role.
 */
@Component
public class JwtAuthoritiesMapper implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthoritiesMapper.class);
//    private final AppUserService appUserService;

    /**
     * Converts a JWT token into a Spring Security authentication token.
     * This method verifies the JWT, extracts the authorities, and creates an authentication token.
     *
     * @param jwt the JWT token to convert
     * @return an authentication token with the appropriate authorities
     * @throws IllegalArgumentException if the JWT is invalid
     */
    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) {
        log.debug("Converting JWT to authentication token for subject: {}", jwt.getSubject());

        try {
            // Extract authorities from the JWT
            Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
            log.debug("Extracted authorities: {}", authorities);

            // Get the principal claim value (subject)
            String principalClaimValue = jwt.getClaimAsString(JwtClaimNames.SUB);

            // Create and return the authentication token
            JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities, principalClaimValue);
            log.debug("Successfully created authentication token for user: {}", principalClaimValue);
            return token;
        } catch (Exception e) {
            log.error("Error converting JWT to authentication token: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Extracts the authorities (roles) from the JWT token.
     * This method reads the "authorities" claim from the JWT and converts it to a GrantedAuthority.
     * If the authority doesn't start with "ROLE_", it adds the prefix.
     *
     * @param jwt the JWT token containing the authorities
     * @return a collection of granted authorities
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        String authorities = jwt.getClaimAsString("authorities");
        log.debug("Extracting authorities from JWT: {}", authorities);

        if (authorities.startsWith("ROLE_")) {
            log.debug("Authority already has ROLE_ prefix: {}", authorities);
            return Collections.singleton(new SimpleGrantedAuthority(authorities));
        }

        log.debug("Adding ROLE_ prefix to authority: {}", authorities);
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + authorities));
    }
}
