package huan.backend.dto.response;

import huan.backend.enumerate.ProjectRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberResponse {
    private Long projectId;
    private String projectName;
    private String userName;
    private ProjectRole projectRole;
}