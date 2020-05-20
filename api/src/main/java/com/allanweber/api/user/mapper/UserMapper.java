package com.allanweber.api.user.mapper;

import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.repository.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserEntity mapToEntity(UserDto dto);

    UserDto mapToDto(UserEntity entity);
}
