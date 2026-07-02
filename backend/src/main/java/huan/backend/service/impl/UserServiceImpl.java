package huan.backend.service.impl;

import huan.backend.dto.request.UserRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.dto.response.UserResponse;
import huan.backend.entity.Member;
import huan.backend.entity.Project;
import huan.backend.entity.User;
import huan.backend.enums.ErrorCode;
import huan.backend.enums.Role;
import huan.backend.exception.AppException;
import huan.backend.mapper.ProjectMapper;
import huan.backend.mapper.UserMapper;
import huan.backend.repository.MemberRepository;
import huan.backend.repository.UserRepository;
import huan.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MemberRepository memberRepository;
    private final ProjectMapper projectMapper;

    @Override
    public UserResponse addAccount(UserRequest accountRequest){
        if(userRepository.existsByEmail(accountRequest.getEmail())||userRepository.existsByPhoneNumber(accountRequest.getPhoneNumber()))
            throw new AppException(ErrorCode.USER_EXISTED);
        
        User user = userMapper.toEntity(accountRequest);
        user.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        user.setRole(Role.USER);
        
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);                                    
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<User> pageData = userRepository.findAll(pageable);

        List<UserResponse> responseList = pageData.getContent().stream()
                .map(userMapper::toResponse)
                .toList();
        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responseList)
                .build();
    }

    @Override
    public UserResponse getAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    public ApiResponse deleteAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setIsActive(false); 
        userRepository.save(user); 
        return ApiResponse.builder()
                .message("Xóa người dùng thành công")
                .build();
    }
    
    @Override
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
}