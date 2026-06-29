package com.example.storefront.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storefront.dto.AuthLoginRequest;
import com.example.storefront.dto.AuthLoginResponse;
import com.example.storefront.dto.AuthSignupRequest;
import com.example.storefront.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "APIs related to authentication")
public class AuthController {

    private final AuthService authService;
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest data) {
        return this.authService.login(data);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Signup")
    public ResponseEntity<AuthLoginResponse> signUp(@Valid @RequestBody AuthSignupRequest dto) {
        return this.authService.signUp(dto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token")
    public ResponseEntity<?> refresh(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refrehToken) {
        return this.authService.refresh(refrehToken);
    }

    @GetMapping("/logout")
    @Operation(summary = "Logout")
    public ResponseEntity<?> logout(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refrehToken) {
        return this.authService.logout(refrehToken);
    }

}
