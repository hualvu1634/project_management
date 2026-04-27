package huan.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String ownerName;
    private Integer teamsize;
    private Boolean isActive;
}