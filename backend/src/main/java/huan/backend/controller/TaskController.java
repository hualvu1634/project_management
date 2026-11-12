package huan.backend.controller;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.request.StatusRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.TaskResponse;
import huan.backend.service.FileUploadService;
import huan.backend.service.TaskService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final FileUploadService fileUploadService;
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.CREATED);
    }
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String savedFileName = fileUploadService.uploadFile(file);
            return ResponseEntity.ok("Lưu file thành công: " + savedFileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống khi lưu file: " + e.getMessage());
        }
    }
    @PutMapping
    public ResponseEntity<TaskResponse> updateTaskStatus(    @RequestBody StatusRequest taskStatusRequest) { 
        return ResponseEntity.ok(taskService.updateTaskStatus(taskStatusRequest));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<PageResponse<TaskResponse>> getTasksByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getTasksByUserId(userId, page, size));
    }
}