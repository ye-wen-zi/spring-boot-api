package com.example.storefront.config;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashidsConfig {

    @Value("${app.security.hashids.salt:dfghjklbbdhjbchjbchdbjauie}")
    private String SALT;

    @Value("${app.security.hashids.min-hash-length:6}")
    private int MIN_HASH_LENGTH;

    @Bean Hashids hashids() {
        return new Hashids(SALT, MIN_HASH_LENGTH);
    }
}
