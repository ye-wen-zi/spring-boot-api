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
import com.example.storefront.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest data) {
        return authService.login(data);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refrehToken) {
        return this.authService.refresh(refrehToken);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refrehToken) {
        return this.authService.logout(refrehToken);
    }

}
