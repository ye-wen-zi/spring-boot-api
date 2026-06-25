package com.example.storefront.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.storefront.dto.AuthLoginResponse;
import com.example.storefront.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    AuthLoginResponse fromEntityToResponse(User user);
}
