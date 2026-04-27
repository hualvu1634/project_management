package huan.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import huan.backend.dto.request.UserRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.UserResponse;

import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.enumerate.Role;
import huan.backend.exception.AppException;
import huan.backend.mapper.UserMapper;
import huan.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse addAccount(UserRequest accountRequest){
        
        if(userRepository.existsByEmail(accountRequest.getEmail())||userRepository.existsByPhoneNumber(accountRequest.getPhoneNumber()))
            throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toEntity(accountRequest);
       
        user.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        user.setRole(Role.USER);
        
        User savedUser = userRepository.save(user);
    
        
        return userMapper.toResponse(savedUser);                                    
    }

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

    public UserResponse getAccount() {
      
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    public ApiResponse deleteAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setIsActive(false); 
        userRepository.save(user); 
        return ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(200)
                .message("Xóa người dùng thành công")
                .build();
    }
}