package huan.backend.service;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.MailResponse;

public interface MemberService {
    MailResponse addMember(MemberRequest request);
    void removeMember(Long id);
}