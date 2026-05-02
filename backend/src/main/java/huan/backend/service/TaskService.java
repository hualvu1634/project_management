package huan.backend.service;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.TaskResponse;
import huan.backend.entity.Project;
import huan.backend.entity.Task;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.enumerate.TaskStatus;
import huan.backend.exception.AppException;
import huan.backend.mapper.TaskMapper;
import huan.backend.repository.MemberRepository;
import huan.backend.repository.ProjectRepository;
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

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Task task = taskMapper.toEntity(request);
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);

        if (request.getAssigneeId() != null) {
            boolean isMember = memberRepository.existsByProjectIdAndUserIdAndIsActiveTrue(
                    project.getId(), 
                    request.getAssigneeId()
            );
            
            if (!isMember) {
                throw new AppException(ErrorCode.ASSIGNEE_NOT_IN_PROJECT);
            }
            
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            task.setAssignee(assignee);
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }
    
    @Transactional
    public TaskResponse updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND)); 
                
        task.setStatus(newStatus);
        return taskMapper.toResponse(taskRepository.save(task));
    }

   
    public PageResponse<TaskResponse> getTasksByProject(Long id, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        
       
        Page<Task> pageData = taskRepository.findByProjectId(id, pageable);

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