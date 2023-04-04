package com.mmacedoaraujo.avaliacaojavaspringdedalus.mapper;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User updateUser(User userWithUpdatedValues, User userFromDatabase);
}
