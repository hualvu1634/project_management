package huan.backend.service;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.MemberResponse;

public interface MemberService {
    MemberResponse addMember(MemberRequest request);
    ApiResponse removeMember(Long id);
}