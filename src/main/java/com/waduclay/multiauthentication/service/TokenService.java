package com.waduclay.multiauthentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 *@author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    private final JwtEncoder jwtEncoder;
    private final JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
    private static final String GENERATING_TOKEN = "Generating authorisation token for {}";
    private static final String TOKEN_GENERATED = "Authorisation token for {} has been successfully generated.";

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }


    public String generateToken(Authentication authentication) {
        log.debug(GENERATING_TOKEN, authentication.getName());
        JwtClaimsSet claims = getClaims(authentication);
        log.debug(TOKEN_GENERATED, authentication.getName());
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    private JwtClaimsSet getClaims(Authentication authentication) {
        Instant now = Instant.now();
        GrantedAuthority grantedAuthority = authentication.getAuthorities().stream().findFirst().orElse(null);
        assert grantedAuthority != null;
        String scope = grantedAuthority.getAuthority();
        return JwtClaimsSet.builder()
                .issuer("multi-authentication")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("authorities", scope)
                .build();
    }

}
