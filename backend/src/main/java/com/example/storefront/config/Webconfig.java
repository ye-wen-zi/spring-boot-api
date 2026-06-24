package com.example.storefront.config;

import org.hashids.Hashids;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.storefront.formatters.HashidsFormatter;

@Configuration
public class Webconfig implements WebMvcConfigurer {
    private final Hashids hashids;

    public Webconfig(Hashids hashids) {
        this.hashids = hashids;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Long.class, new HashidsFormatter(hashids));
    }
}
