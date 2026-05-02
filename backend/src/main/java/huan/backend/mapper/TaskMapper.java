package huan.backend.mapper;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.response.TaskResponse;
import huan.backend.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "assigneeName", source = "assignee.name")
    TaskResponse toResponse(Task task);

    // Map từ Request vào Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)  
    @Mapping(target = "assignee", ignore = true) 
    @Mapping(target = "status",ignore = true)
    Task toEntity(TaskRequest request);
}