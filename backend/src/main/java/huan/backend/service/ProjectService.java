package huan.backend.service;

import huan.backend.dto.request.ProjectRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.MemberProjectResponse;
import huan.backend.dto.response.MemberResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.dto.response.TaskResponse;
import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.Task;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.enumerate.ProjectRole;
import huan.backend.exception.AppException;
import huan.backend.mapper.MemberMapper;
import huan.backend.mapper.ProjectMapper;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    public ProjectResponse createProject(ProjectRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Project project = projectMapper.toEntity(request);
        project.setOwner(owner);
        Project save = projectRepository.save(project);
        Member pmMember = Member.builder()
        .projectRole(ProjectRole.PROJECT_MANAGER)
                .project(save)
                .user(owner)
                .isActive(true)
                .build();
        memberRepository.save(pmMember);
        return projectMapper.toResponse(save);
    }

    
    public MemberProjectResponse getMembersByProject(Long id, int page, int size) {
     
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        List<Member> pageData = memberRepository.findByProjectIdAndIsActiveTrue(id);

        List<MemberResponse> responseList = pageData.stream()
                .map(memberMapper::toResponse)
                .collect(Collectors.toList());

        return MemberProjectResponse.builder()
                .name(project.getName())
                .memberResponses(responseList)
                .build();
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


    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        
        return projectMapper.toResponse(projectRepository.save(project));
    }

    public ApiResponse deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        project.setIsActive(false);
        projectRepository.save(project);
        return ApiResponse.builder().timestamp(LocalDateTime.now())
                .code(200)
                .message("Xóa dự án thành công")
                .build();
    }
}