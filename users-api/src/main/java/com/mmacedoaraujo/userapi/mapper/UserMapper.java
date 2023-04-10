package com.mmacedoaraujo.userapi.mapper;

import com.mmacedoaraujo.userapi.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

//Here is defined the strategy for the mapper ignore null properties at an User object when mapping to another
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //When updating an User entity, this maps an user with modifications to another instance of user, ignoring null fields
    User updateUser(User userWithUpdatedValues, @MappingTarget User userFromDatabase);
}
