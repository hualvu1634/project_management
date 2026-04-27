package huan.backend.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import huan.backend.dto.request.UserRequest;
import huan.backend.dto.response.UserResponse;
import huan.backend.entity.User;

@Mapper(componentModel = "spring") 
public interface UserMapper {
      // @Mapping(target = "id", ignore = true) 
    UserResponse toResponse(User user);
    @Mapping(target = "password", ignore = true) 
    @Mapping(target = "role", ignore = true)
   @Mapping(target = "id", ignore = true) 
    @Mapping(target = "isActive", ignore = true) 
    User toEntity(UserRequest request);
}
