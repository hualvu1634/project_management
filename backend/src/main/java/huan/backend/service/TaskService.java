package huan.backend.service;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.request.TaskStatusRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.TaskResponse;
import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.Task;
import huan.backend.entity.TaskLog;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.enumerate.ProjectRole;
import huan.backend.enumerate.TaskStatus;
import huan.backend.exception.AppException;
import huan.backend.mapper.TaskMapper;
import huan.backend.repository.MemberRepository;
import huan.backend.repository.ProjectRepository;
import huan.backend.repository.TaskLogRepository;
import huan.backend.repository.TaskRepository;
import huan.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final TaskMapper taskMapper;
    private final TaskLogRepository taskLogRepository;

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Task task = taskMapper.toEntity(request);
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);

            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            task.setAssignee(assignee);
        
        Task savedTask = taskRepository.save(task);

        
        TaskLog log = TaskLog.builder()
                .task(savedTask)
                .user(assignee) 
                .status(TaskStatus.TODO) 
                .build();
        taskLogRepository.save(log);

        TaskResponse response = taskMapper.toResponse(savedTask);

        return response;
    }
    
    @Transactional
public TaskResponse updateTaskStatus( TaskStatusRequest request) {
    
    Task task = taskRepository.findById(request.getTaskId())
            .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));
            
    Member currentMember = memberRepository.findById(request.getUserId()).orElseThrow(()-> new AppException(ErrorCode.MEMBER_NOT_FOUND));

    ProjectRole role = currentMember.getProjectRole();
    Long assigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;

    if (role != ProjectRole.PROJECT_MANAGER) { 
        if (request.getStatus() == TaskStatus.DONE) {
            if (role != ProjectRole.TESTER) {
                throw new AppException(ErrorCode.UNAUTHORIZED); 
            }
        } 
        else if (request.getStatus() == TaskStatus.IN_PROGRESS || request.getStatus() == TaskStatus.IN_REVIEW) {
            if (assigneeId == null || !assigneeId.equals(request.getUserId())) {
                throw new AppException(ErrorCode.UNAUTHORIZED); 
            }
        }
    }

    task.setStatus(request.getStatus());
    return taskMapper.toResponse(taskRepository.save(task));
}


  
public PageResponse<TaskResponse> getTasksByUserId(Long userId, int page, int size) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
    
    Page<Task> pageData = taskRepository.findByAssigneeId(userId, pageable);

    List<TaskResponse> responseList = pageData.getContent().stream()
            .map(taskMapper::toResponse)
            .collect(Collectors.toList());

    return PageResponse.<TaskResponse>builder()
            .currentPage(page)
            .pageSize(pageData.getSize())
            .totalPages(pageData.getTotalPages())
            .totalElements(pageData.getTotalElements())
            .data(responseList)
            .build();
}
}