package huan.backend.service.impl;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.MailResponse;

import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.User;
import huan.backend.enums.ErrorCode;
import huan.backend.enums.ProjectRole;
import huan.backend.exception.AppException;
import huan.backend.mapper.MemberMapper;
import huan.backend.repository.MemberRepository;
import huan.backend.repository.ProjectRepository;
import huan.backend.repository.UserRepository;
import huan.backend.service.EmailService;
import huan.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MemberMapper memberMapper;

    @Override
    public MailResponse addMember(MemberRequest request) {
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
        member.setProjectRole(ProjectRole.MEMBER);
        project.setTeamsize(project.getTeamsize() + 1);
        projectRepository.save(project);
        memberRepository.save(member);
        
        MailResponse mailResponse = MailResponse.builder()
                                .to(user.getEmail())
                                .subject("Chúc mừng")
                                .text("Bạn đã  tham gia vào dự án " + project.getName()).build();
                                
        emailService.sendEmail(mailResponse);
        return mailResponse;
   
    }

    @Override
    public ApiResponse removeMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->  new AppException(ErrorCode.MEMBER_NOT_FOUND));
        
        member.setIsActive(false);
        memberRepository.save(member);

        Project project = member.getProject();
        if (project.getTeamsize() > 0) {
            project.setTeamsize(project.getTeamsize() - 1);
            projectRepository.save(project);
        }
        return ApiResponse.builder()
                .message("Xóa thành viên thành công")
                .build();
    }
}