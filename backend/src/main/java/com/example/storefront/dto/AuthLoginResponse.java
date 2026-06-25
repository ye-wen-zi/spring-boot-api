package com.example.storefront.dto;

import com.example.storefront.constants.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthLoginResponse {
        private String accessToken;
        private String refreshToken;
        private String firstName;
        private String lastName;
        private String email;
        private Role role;
}
