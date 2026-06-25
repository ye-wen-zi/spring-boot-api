package com.example.storefront.filters;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.storefront.repositories.UserRepository;
import com.example.storefront.utils.JwtTokenUntils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUntils jwtTokenUntils;
    private final UserRepository userRepository;

    public JwtTokenFilter(JwtTokenUntils jwtTokenUntils, UserRepository userRepository) {
        this.jwtTokenUntils = jwtTokenUntils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = header.substring(7).trim();
            if (!jwtTokenUntils.validate(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // @TODO: USE REDIS BEFORE QUERY DB
            UserDetails userDetails = this.userRepository.findByEmail(jwtTokenUntils.getUsername(token)).orElse(null);

            log.info("User: {}", userDetails);

            if (userDetails != null) {
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("Authenticated: {}", authentication);
            }

        } catch (Exception e) {
            log.error("Unable to authenticate!: {}", e);
        }
        filterChain.doFilter(request, response);
    }

}
