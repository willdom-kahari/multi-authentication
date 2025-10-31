package com.waduclay.multiauthentication.controller;


import com.waduclay.multiauthentication.dto.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@RestController
@Tag(name = "Services")
public class AppController {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> adminGreeting(){
        return ResponseEntity.ok(new GenericResponse(true, "Hello from Admin"));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GenericResponse> userGreeting(){
        return ResponseEntity.ok(new GenericResponse(true, "Hello from User"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GenericResponse> handleException(AccessDeniedException e){
        return new ResponseEntity<>(new GenericResponse(false, e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
