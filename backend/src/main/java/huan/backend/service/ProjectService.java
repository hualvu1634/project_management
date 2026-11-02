package huan.backend.service;

import java.util.List;

import huan.backend.dto.request.ProjectRequest;
import huan.backend.dto.response.MemberResponse;

import huan.backend.dto.response.ProjectResponse;
import huan.backend.dto.response.TaskResponse;

public interface ProjectService {
    ProjectResponse createProject(ProjectRequest request);
    List<MemberResponse> getMembersByProject(Long id);
    List<TaskResponse> getTasksByProject(Long id);
    ProjectResponse updateProject(Long id, ProjectRequest request);
    void deleteProject(Long id);
}