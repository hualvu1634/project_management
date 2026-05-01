package huan.backend.service;

import huan.backend.dto.request.ProjectRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;

import huan.backend.exception.AppException;
import huan.backend.mapper.ProjectMapper;
import huan.backend.repository.MemberRepository;
import huan.backend.repository.ProjectRepository;
import huan.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Project project = projectMapper.toEntity(request);
        project.setOwner(owner);
        Project save = projectRepository.save(project);
        Member pmMember = Member.builder()
                .project(save)
                .user(owner)
                .isActive(true)
                .build();
        memberRepository.save(pmMember);
        return projectMapper.toResponse(save);
    }

public PageResponse<ProjectResponse> getProjectsByUserId(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    

    Page<Member> memberPage = memberRepository.findByUserIdAndIsActiveTrue(userId, pageable);

    List<ProjectResponse> responseList = memberPage.getContent().stream()
            .map(Member::getProject) 
            .filter(Project::getIsActive) 
            .map(projectMapper::toResponse)
            .collect(Collectors.toList());

    return PageResponse.<ProjectResponse>builder()
            .currentPage(page)
            .pageSize(memberPage.getSize())
            .totalPages(memberPage.getTotalPages())
            .totalElements(memberPage.getTotalElements())
            .data(responseList)
            .build();
}
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Transactional
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