package com.allanweber.api.user.mapper;

import com.allanweber.api.registration.UserRegistration;
import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.repository.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserEntity mapToEntity(UserRegistration registration);

    UserDto mapToDto(UserEntity entity);
}
