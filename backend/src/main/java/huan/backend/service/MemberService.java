package huan.backend.service;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.MemberResponse;
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

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ApiResponse removeMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->  new AppException(ErrorCode.MEMBER_NOT_FOUND));
        
        // Soft delete thành viên
        member.setIsActive(false);
        memberRepository.save(member);

        // Tự động giảm teamsize
        Project project = member.getProject();
        if (project.getTeamsize() > 0) {
            project.setTeamsize(project.getTeamsize() - 1);
            projectRepository.save(project);
                
        }
         return ApiResponse.builder().timestamp(LocalDateTime.now())
                .code(200)
                .message("Xóa dự án thành công")
                .build();
    }
}