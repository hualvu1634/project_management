package huan.backend.service;

import huan.backend.dto.request.UserRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.dto.response.UserResponse;

public interface UserService {
    UserResponse addAccount(UserRequest accountRequest);
    PageResponse<UserResponse> getAllUsers(int page, int size);
    UserResponse getAccount();
    void deleteAccount(Long id);
    PageResponse<ProjectResponse> getProjectsByUserId(Long userId, int page, int size);
}