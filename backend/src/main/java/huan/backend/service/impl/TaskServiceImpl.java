package huan.backend.service.impl;

import huan.backend.dto.request.TaskRequest;
import huan.backend.dto.request.StatusRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.TaskResponse;
import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.Task;
import huan.backend.entity.TaskLog;
import huan.backend.entity.User;
import huan.backend.enums.ErrorCode;
import huan.backend.enums.ProjectRole;
import huan.backend.enums.TaskStatus;
import huan.backend.exception.AppException;
import huan.backend.mapper.TaskMapper;
import huan.backend.repository.MemberRepository;
import huan.backend.repository.ProjectRepository;
import huan.backend.repository.TaskLogRepository;
import huan.backend.repository.TaskRepository;
import huan.backend.service.TaskService;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private final MemberRepository memberRepository;
    private final TaskMapper taskMapper;
    private final TaskLogRepository taskLogRepository;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Task task = taskMapper.toEntity(request);
        task.setCreateDate(LocalDate.now());
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);

        Member assignee = memberRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
      User user = assignee.getUser();
        task.setAssignee(assignee);
        
        Task savedTask = taskRepository.save(task);
        
        TaskLog log = TaskLog.builder()
                .task(savedTask)
                .member(assignee) 
                .status(TaskStatus.TODO) 
                .createdAt(LocalDate.now())
                .build();
        taskLogRepository.save(log);
        TaskResponse taskResponse = taskMapper.toResponse(savedTask);
        taskResponse.setAssigneeName(user.getName());
        return taskMapper.toResponse(savedTask);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "user_tasks", allEntries = true)
    public TaskResponse updateTaskStatus(StatusRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));
                
        Member currentMember = memberRepository.findById(request.getId()).orElseThrow(()-> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        ProjectRole role = currentMember.getProjectRole();
        Long assigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;

        if (role != ProjectRole.PROJECT_MANAGER) { 
            if (request.getStatus() == TaskStatus.DONE) {
                if (role != ProjectRole.MEMBER) {
                    throw new AppException(ErrorCode.UNAUTHORIZED); 
                }
            } 
            else if (request.getStatus() == TaskStatus.IN_PROGRESS || request.getStatus() == TaskStatus.IN_REVIEW) {
                if (assigneeId == null || !assigneeId.equals(request.getId())) {
                    throw new AppException(ErrorCode.UNAUTHORIZED); 
                }
            }
        }
        
        Member assignee = memberRepository.findById(request.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        TaskLog log = TaskLog.builder()
                .task(task)
                .member(assignee) 
                .status(request.getStatus()) 
                .build();
        taskLogRepository.save(log);
        task.setStatus(request.getStatus());
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    @Cacheable(value = "user_tasks", key = "#userId + '-' + #page + '-' + #size")
    public PageResponse<TaskResponse> getTasksByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        
        Page<Task> pageData = taskRepository.findTasksByAssignee(userId, pageable);

        List<TaskResponse> responseList = pageData.getContent().stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<TaskResponse>builder()
                .current(page)
                .size(pageData.getSize())
                .total(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responseList)
                .build();
    }
}