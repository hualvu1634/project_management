package huan.backend.service;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.MemberResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.exception.AppException;
import huan.backend.mapper.MemberMapper;
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

    @Transactional
    public MemberResponse addMember(MemberRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (memberRepository.existsByProjectIdAndUserIdAndIsActiveTrue(project.getId(), user.getId())) {
            throw new AppException(ErrorCode.MEMBER_EXISTED);
        }

        Member member = memberMapper.toEntity(request);
        member.setProject(project);
        member.setUser(user);
        project.setTeamsize(project.getTeamsize() + 1);
        projectRepository.save(project);

        return memberMapper.toResponse(memberRepository.save(member));
    }

    public PageResponse<MemberResponse> getMembersByProject(Long projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        
        Page<Member> pageData = memberRepository.findByProjectIdAndIsActiveTrue(projectId, pageable);

        List<MemberResponse> responseList = pageData.getContent().stream()
                .map(memberMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<MemberResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
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