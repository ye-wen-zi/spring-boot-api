package com.example.storefront.services;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.storefront.dto.AuthLoginRequest;
import com.example.storefront.dto.AuthLoginResponse;
import com.example.storefront.dto.AuthSignupRequest;
import com.example.storefront.entities.User;
import com.example.storefront.repositories.UserRepository;
import com.example.storefront.utils.JwtTokenUntils;
import com.example.storefront.exceptions.BadRequestException;
import com.example.storefront.exceptions.UnauthorizedException;
import com.example.storefront.mappers.UserMapper;

@Service
public class AuthService {
        private final UserRepository userRepository;
        private final UserMapper userMapper;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenUntils jwtTokenUntils;
        private final RedisTokenService redisTokenService;

        @Value("${app.security.jwt.access-token-expiration:900000}")
        private long ACCESS_TOKEN_EXPIRATION;

        @Value("${app.security.jwt.refresh-token-expiration:86400000}") // 1 day -> ms
        private long REFRESH_TOKEN_EXPIRATION;

        @Value("${app.security.secure-cookie:true}")
        private boolean isSecureCookie;

        private final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

        public AuthService(UserRepository userRepository, UserMapper userMapper,
                        PasswordEncoder passwordEncoder,
                        JwtTokenUntils jwtTokenUntils,
                        RedisTokenService redisTokenService) {
                this.userRepository = userRepository;
                this.userMapper = userMapper;
                this.passwordEncoder = passwordEncoder;
                this.jwtTokenUntils = jwtTokenUntils;
                this.redisTokenService = redisTokenService;
        }

        public ResponseCookie createCookies(String refreshToken, long maxAge) {
                return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                                .httpOnly(true) // XSS
                                .secure(isSecureCookie)
                                .maxAge(maxAge)
                                .path("/")
                                // .sameSite("Strict") // CSRF
                                .build();
        }

        public ResponseEntity<AuthLoginResponse> signUp(AuthSignupRequest dto) {
                var existingUser = this.userRepository.findByEmail(dto.email());
                if (existingUser.isPresent()) {
                        throw new BadRequestException("The email is already in use.");
                }

                User newUser = this.userMapper.fromRequestToEntity(dto);
                newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));

                User savedUser = this.userRepository.save(newUser);
                var authInfo = new AuthLoginRequest(dto.email(), dto.password());
                return this._login(authInfo, savedUser);

        }

        public ResponseEntity<AuthLoginResponse> login(AuthLoginRequest authInfo) {
                String errorMessage = "Invalid credentials!";
                var user = this.userRepository.findByEmail(authInfo.email())
                                .orElseThrow(() -> new UnauthorizedException(errorMessage));
                return this._login(authInfo, user);
        }

        private ResponseEntity<AuthLoginResponse> _login(AuthLoginRequest authInfo, User user) {
                String errorMessage = "Invalid credentials!";
                if (passwordEncoder.matches(authInfo.password(), user.getPassword())) {
                        AuthLoginResponse authLoginResponse = userMapper.fromEntityToResponse(user);

                        String accessToken = jwtTokenUntils.generateToken(user.getEmail(), ACCESS_TOKEN_EXPIRATION);
                        String refeshToken = jwtTokenUntils.generateToken(user.getEmail(), REFRESH_TOKEN_EXPIRATION);

                        this.redisTokenService.saveRefreshToken(user.getEmail(), refeshToken, REFRESH_TOKEN_EXPIRATION,
                                        TimeUnit.MILLISECONDS);

                        authLoginResponse.setAccessToken(accessToken);
                        authLoginResponse.setRefreshToken(refeshToken);

                        return ResponseEntity
                                        .ok()
                                        .header(HttpHeaders.SET_COOKIE,
                                                        createCookies(refeshToken, REFRESH_TOKEN_EXPIRATION / 1000)
                                                                        .toString())
                                        .body(authLoginResponse);

                }

                throw new UnauthorizedException(errorMessage);
        }

        public ResponseEntity<?> refresh(String refreshToken) {
                if (!jwtTokenUntils.validate(refreshToken)) {
                        throw new UnauthorizedException("Invalid token!");
                }

                String email = jwtTokenUntils.getUsername(refreshToken);
                String savedRefeshToken = this.redisTokenService.getRefreshToken(email);

                if (savedRefeshToken == null || !savedRefeshToken.equals(savedRefeshToken)) {
                        throw new UnauthorizedException("Invalid token!");
                }

                String newAccessToken = this.jwtTokenUntils.generateToken(email, ACCESS_TOKEN_EXPIRATION);
                String newRefreshToken = this.jwtTokenUntils.generateToken(email, REFRESH_TOKEN_EXPIRATION);

                this.redisTokenService.saveRefreshToken(email, newRefreshToken, REFRESH_TOKEN_EXPIRATION,
                                TimeUnit.MILLISECONDS);

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE,
                                                this.createCookies(newRefreshToken, REFRESH_TOKEN_EXPIRATION / 1000)
                                                                .toString())
                                .body(Map.of("accessToken", newAccessToken));
        }

        public ResponseEntity<?> logout(String refreshToken) {
                String email = this.jwtTokenUntils.getUsername(refreshToken);
                this.redisTokenService.deleteRefreshToken(email);
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, this.createCookies("", 0).toString())
                                .body(Map.of("message", "Logged out"));
        }
}
