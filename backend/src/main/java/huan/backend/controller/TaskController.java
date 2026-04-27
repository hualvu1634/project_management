package huan.backend.controller;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.response.TaskResponse;
import huan.backend.enumerate.TaskStatus;
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

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long id, 
            @RequestParam TaskStatus status) { // Dùng @RequestParam để truyền trạng thái trực tiếp trên URL 
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }
}