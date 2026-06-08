package huan.backend.service;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.request.StatusRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.TaskResponse;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    TaskResponse updateTaskStatus(StatusRequest request);
    PageResponse<TaskResponse> getTasksByUserId(Long userId, int page, int size);
}