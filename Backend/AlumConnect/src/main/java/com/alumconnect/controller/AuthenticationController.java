package com.alumconnect.controller;

import com.alumconnect.dto.LoginRequest;
import com.alumconnect.dto.RegisterRequest;
import com.alumconnect.exception.ACException;
import com.alumconnect.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class responsible for user authentication and registration.
 */
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object>  register(
           @NotNull @RequestBody RegisterRequest request
    ) {
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

@PostMapping("/login")
public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
    try {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword()));
    } catch (ACException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

}
