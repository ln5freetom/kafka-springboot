package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.UserRequest;
import com.example.ecommerce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest request);
    void updateEntity(UserRequest request, @MappingTarget User user);
}
