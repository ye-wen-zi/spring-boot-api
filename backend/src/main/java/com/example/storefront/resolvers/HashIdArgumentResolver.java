package com.example.storefront.resolvers;

import org.hashids.Hashids;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class HashIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final Hashids hashids;

    public HashIdArgumentResolver(Hashids hashids) {
        this.hashids = hashids;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(HashId.class) &&
                parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Map<String, String> pathVariables = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);

        String paramName = parameter.getParameterName();
        String hashValue = pathVariables != null ? pathVariables.get(paramName) : null;

        log.info("pathVariables {}", pathVariables);
        log.info("hashValue {}", hashValue);
        log.info("paramName {}", paramName);

        if (hashValue == null || hashValue.isBlank()) {
            return null;
        }

        try {
            long[] decoded = hashids.decode(hashValue);
            if (decoded.length > 0) {
                return decoded[0];
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Hash ID format");
        }

        return null;
    }
}