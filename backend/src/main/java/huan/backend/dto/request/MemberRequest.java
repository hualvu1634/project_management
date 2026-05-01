package huan.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequest {
    private Long projectId;
    private Long userId;
}