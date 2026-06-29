package com.example.storefront.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.storefront.entities.CustomUserDetails;
import com.example.storefront.exceptions.ResourceNotFoundException;
import com.example.storefront.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RedisService redisService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String key = "user_details:" + username;

        UserDetails userDetails = this.redisService.get(key, CustomUserDetails.class);
        if (userDetails != null) {
            log.info("Cached user details from redis: {}", userDetails.getUsername());
            return userDetails;
        }

        var user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist!"));

        userDetails = CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(List.of(user.getRole().name()))
                .build();

        this.redisService.put(key, userDetails, 30, TimeUnit.MINUTES);

        log.info("Loaded user from db: {}", userDetails.getUsername());

        return userDetails;
    }

}
