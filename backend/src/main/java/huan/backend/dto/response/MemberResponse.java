package huan.backend.dto.response;

import java.io.Serializable;

import huan.backend.enums.ProjectRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberResponse  implements Serializable{
   
    private String userName;
    private ProjectRole projectRole;
}