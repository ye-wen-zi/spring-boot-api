package com.example.storefront.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.storefront.dto.AuthLoginResponse;
import com.example.storefront.dto.AuthSignupRequest;
import com.example.storefront.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    AuthLoginResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "isLocked", constant = "false")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "cart", ignore = true)
    User fromRequest(AuthSignupRequest dto);
}
