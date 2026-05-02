package huan.backend.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberProjectResponse {
    private String name;
    private List<MemberResponse> memberResponses;
}
