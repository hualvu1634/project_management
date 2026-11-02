package huan.backend.service.impl;

import huan.backend.dto.request.ProjectRequest;
import huan.backend.dto.response.MemberResponse;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.dto.response.TaskResponse;
import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.Task;
import huan.backend.entity.User;
import huan.backend.enums.ErrorCode;
import huan.backend.enums.ProjectRole;
import huan.backend.exception.AppException;
import huan.backend.mapper.MemberMapper;
import huan.backend.mapper.ProjectMapper;
import huan.backend.mapper.TaskMapper;
import huan.backend.repository.MemberRepository;
import huan.backend.repository.ProjectRepository;
import huan.backend.repository.TaskRepository;
import huan.backend.repository.UserRepository;
import huan.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @CacheEvict(value = {"user_projects", "projects"}, allEntries = true)
    public ProjectResponse createProject(ProjectRequest request) {
        User user = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Project project = projectMapper.toEntity(request);
        project.setOwner(user);
        Project save = projectRepository.save(project);
        Member pmMember = Member.builder()
                .projectRole(ProjectRole.PROJECT_MANAGER)
                .project(save)
                .user(user)
                .isActive(true)
                .build();
        memberRepository.save(pmMember);
        return projectMapper.toResponse(save);
    }

    @Override
    @Cacheable(value = "project_members", key = "#id")
    public List<MemberResponse> getMembersByProject(Long id) {


        List<Member> pageData = memberRepository.findActiveByProject(id);

        return pageData.stream()
                .map(memberMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "project_tasks", key = "#id")
    public List<TaskResponse> getTasksByProject(Long id) {
        List<Task> pageData = taskRepository.findByProjectId(id);
        log.info("Tìm thấy {} task cho project ID: {}", pageData.size(), id);
        List<TaskResponse> taskResponses = pageData.stream()
                .map(taskMapper::toResponse)
                .toList();
        return taskResponses;
    }

    @Override
    @CacheEvict(value = {"user_projects", "projects"}, allEntries = true)
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @CacheEvict(value = {"user_projects", "projects", "project_members", "project_tasks"}, allEntries = true)
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        project.setIsActive(false);
        projectRepository.save(project);
    }
}