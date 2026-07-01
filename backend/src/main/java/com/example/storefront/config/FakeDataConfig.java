package com.example.storefront.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.datafaker.Faker;

@Configuration
public class FakeDataConfig {
    @Bean
    public Faker createFaker() {
        return new Faker();
    }
}
