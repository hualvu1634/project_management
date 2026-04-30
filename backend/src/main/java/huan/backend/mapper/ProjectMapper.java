package huan.backend.mapper;

import huan.backend.dto.request.ProjectRequest;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    
    ProjectResponse toResponse(Project project);

  
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true) 
    @Mapping(target = "isActive", constant = "true") 
    @Mapping(target = "teamsize", constant = "1")  
    Project toEntity(ProjectRequest request);
}