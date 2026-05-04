package huan.backend.controller;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.request.TaskStatusRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.TaskResponse;

import huan.backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.CREATED);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<PageResponse<TaskResponse>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, page, size));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable  Long id, 
            @RequestBody TaskStatusRequest taskStatusRequest) { 
        return ResponseEntity.ok(taskService.updateTaskStatus(id, taskStatusRequest));
    }
}