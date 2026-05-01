package huan.backend.mapper;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.response.TaskResponse;
import huan.backend.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // Map từ Entity ra Response
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "assigneeName", source = "assignee.name")
    TaskResponse toResponse(Task task);

    // Map từ Request vào Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)  // Sẽ được set ở Service
    @Mapping(target = "assignee", ignore = true) // Sẽ được set ở Service

    @Mapping(target = "status",ignore = true)
    Task toEntity(TaskRequest request);
}