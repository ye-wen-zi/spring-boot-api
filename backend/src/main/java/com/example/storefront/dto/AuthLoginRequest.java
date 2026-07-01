package com.example.storefront.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(toBuilder = true)
public record AuthLoginRequest(
        @Email String email,
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters long.") String password) {
}
