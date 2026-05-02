package huan.backend.mapper;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.MemberResponse;
import huan.backend.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {


    @Mapping(target = "userName", source = "user.name")
    MemberResponse toResponse(Member member);

    // Map từ Request vào Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true) // Sẽ được set ở Service
    @Mapping(target = "user", ignore = true)    // Sẽ được set ở Service
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "projectRole",ignore = true)
    Member toEntity(MemberRequest request);
}