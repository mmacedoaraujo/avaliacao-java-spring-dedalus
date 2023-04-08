package com.mmacedoaraujo.userapi.mapper;

import com.mmacedoaraujo.userapi.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User updateUser(User userWithUpdatedValues, @MappingTarget User userFromDatabase);
}
