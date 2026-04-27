package huan.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequest {
    private String name;
    private String description;
    private Long ownerId; 

}