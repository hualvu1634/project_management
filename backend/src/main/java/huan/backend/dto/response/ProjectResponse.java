package huan.backend.dto.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
}