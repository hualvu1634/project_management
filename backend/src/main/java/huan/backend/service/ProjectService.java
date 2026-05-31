package huan.backend.service;

import huan.backend.dto.request.ProjectRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.MemberProjectResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.dto.response.TaskResponse;

public interface ProjectService {
    ProjectResponse createProject(ProjectRequest request);
    MemberProjectResponse getMembersByProject(Long id, int page, int size);
    PageResponse<TaskResponse> getTasksByProject(Long id, int page, int size);
    ProjectResponse updateProject(Long id, ProjectRequest request);
    ApiResponse deleteProject(Long id);
}