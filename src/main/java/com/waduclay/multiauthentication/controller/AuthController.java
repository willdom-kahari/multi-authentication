package com.waduclay.multiauthentication.controller;


import com.waduclay.multiauthentication.dto.AuthenticationRequest;
import com.waduclay.multiauthentication.dto.AuthenticationResponse;
import com.waduclay.multiauthentication.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@RestController
@RequestMapping("/auth")
@SecurityRequirements
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    ResponseEntity<AuthenticationResponse> auth(@RequestBody AuthenticationRequest authRequest) {
        AuthenticationResponse response = authenticationService.authenticate(authRequest);
        return ResponseEntity.ok(response);
    }
}
