package huan.backend.dto.response;

import huan.backend.enumerate.ProjectRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberResponse {
    private Long id;
    private Long projectId;
    private String projectName;
    private Long userId;
    private String userName;
    private ProjectRole projectRole;
    private Boolean isActive;
}