package huan.backend.service;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.request.SubmitRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.TaskResponse;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    void submitTask(SubmitRequest request);
    PageResponse<TaskResponse> getTasksByUserId(Long userId, int page, int size);
    void deleteTask(Long taskId);
}