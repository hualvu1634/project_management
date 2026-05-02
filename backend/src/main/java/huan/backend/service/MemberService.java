package huan.backend.service;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.MemberProjectResponse; 
import huan.backend.dto.response.MemberResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.enumerate.ProjectRole;
import huan.backend.exception.AppException;
import huan.backend.mapper.MemberMapper;
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
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MemberMapper memberMapper;
    private final ProjectMapper projectMapper;

    @Transactional
    public MemberResponse addMember(MemberRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (memberRepository.existsByProjectIdAndUserIdAndIsActiveTrue(project.getId(), user.getId())) {
            throw new AppException(ErrorCode.MEMBER_EXISTED);
        }

        Member member = memberMapper.toEntity(request);
        member.setProject(project);
        member.setUser(user);
        member.setProjectRole(ProjectRole.DEVELOPER);
        project.setTeamsize(project.getTeamsize() + 1);
        projectRepository.save(project);

        return memberMapper.toResponse(memberRepository.save(member));
    }


    public MemberProjectResponse getMembersByProject(Long projectId, int page, int size) {
     
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        List<Member> pageData = memberRepository.findByProjectIdAndIsActiveTrue(projectId);

        List<MemberResponse> responseList = pageData.stream()
                .map(memberMapper::toResponse)
                .collect(Collectors.toList());

        // 4. Build và trả về MemberProjectResponse
        return MemberProjectResponse.builder()
                .name(project.getName())
                .memberResponses(responseList)
                .build();
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
    public ApiResponse removeMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->  new AppException(ErrorCode.MEMBER_NOT_FOUND));
        
        member.setIsActive(false);
        memberRepository.save(member);

        Project project = member.getProject();
        if (project.getTeamsize() > 0) {
            project.setTeamsize(project.getTeamsize() - 1);
            projectRepository.save(project);
        }
        return ApiResponse.builder().timestamp(LocalDateTime.now())
                .code(200)
                .message("Xóa thành viên thành công")
                .build();
    }
}