package com.waduclay.multiauthentication.service;

import com.waduclay.multiauthentication.dto.AuthenticationRequest;
import com.waduclay.multiauthentication.dto.AuthenticationResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


/**
 *@author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates a user with the provided credentials and generates authentication tokens.
     *
     * @param request The authentication request containing a username and password
     * @return AuthenticationResponse containing access and refresh tokens
     * @throws BadCredentialsException if the credentials are invalid
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authentication request received for user: {}", request.username());

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        log.debug("Generating and saving access token for user: {}", request.username());
        String token = tokenService.generateToken(authenticate);


        log.info("Authentication successful for user: {}", request.username());
        return new AuthenticationResponse(true, token);
    }

}
