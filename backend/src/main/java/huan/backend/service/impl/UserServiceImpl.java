package huan.backend.service.impl;

import huan.backend.dto.request.UserRequest;

import huan.backend.dto.response.ProjectResponse;
import huan.backend.dto.response.UserResponse;
import huan.backend.entity.Project;
import huan.backend.entity.User;
import huan.backend.enums.ErrorCode;
import huan.backend.enums.Role;
import huan.backend.exception.AppException;
import huan.backend.mapper.ProjectMapper;
import huan.backend.mapper.UserMapper;
import huan.backend.repository.ProjectRepository;
import huan.backend.repository.UserRepository;
import huan.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    @Override
    @CacheEvict(value = "users", allEntries = true)
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
    public UserResponse getAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    
    @Override
    @Cacheable(value = "user_projects", key = "#userId")
    public List<ProjectResponse> getProjects(Long userId) {
        List<Project> list = projectRepository.findByOwnerId(userId);
        List<ProjectResponse> responseList = list.stream()
                .map(projectMapper::toResponse)
                .toList();
        return responseList;
    }
}