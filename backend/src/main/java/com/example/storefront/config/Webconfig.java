package com.example.storefront.config;

import java.util.List;

import org.hashids.Hashids;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.storefront.resolvers.HashIdArgumentResolver;

@Configuration
public class Webconfig implements WebMvcConfigurer {
    private final Hashids hashids;

    public Webconfig(Hashids hashids) {
        this.hashids = hashids;
    }

    // @Override
    // public void addFormatters(FormatterRegistry registry) {
    // registry.addFormatterForFieldType(Long.class, new HashidsFormatter(hashids));
    // }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new HashIdArgumentResolver(hashids));
    }
}
