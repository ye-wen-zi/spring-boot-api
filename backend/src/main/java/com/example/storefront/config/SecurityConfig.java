package com.example.storefront.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.storefront.constants.Role;
import com.example.storefront.filters.JwtTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final ObjectMapper objectMapper;

    SecurityConfig(JwtTokenFilter jwtTokenFilter, ObjectMapper objectMapper) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                // .csrf(csrf -> csrf.disable())
                // Tắt CSRF bảo vệ riêng cho H2 Console để có thể submit form đăng nhập h2
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(PathRequest.toH2Console())
                        .disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter()
                                    .write(this.objectMapper.writeValueAsString(Map.of("message", "Unauthrized!")));
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter()
                                    .write(this.objectMapper.writeValueAsString(Map.of("message", "Access forbiden!")));
                        }))

                .authorizeHttpRequests(auth -> auth
                        // Cho phép truy cập vào H2 Console mà không cần đăng nhập
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                // Cho phép hiển thị giao diện H2 bên trong thẻ <iframe>
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();

        config.setAllowCredentials(true);

        config.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}