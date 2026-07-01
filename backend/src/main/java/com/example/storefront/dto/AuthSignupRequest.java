package com.example.storefront.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;


@Builder(toBuilder = true)
public record AuthSignupRequest(
        @NotBlank(message = "First name cannot be blank") String firstName,
        @NotBlank(message = "Last name cannot be blank") String lastName,
        @Email String email,
        @NotBlank(message = "Password cannot be blank") @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long, include at least one uppercase letter, one number, one special character, and contain no spaces.") String password) {

}
