package huan.backend.dto.response;

import huan.backend.enumerate.ProjectRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberResponse {
   
    private String userName;
    private ProjectRole projectRole;
}