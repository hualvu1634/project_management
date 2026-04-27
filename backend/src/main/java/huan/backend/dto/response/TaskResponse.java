package huan.backend.dto.response;

import huan.backend.enumerate.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskResponse {
    private Long id;
    private Long projectId;
    private String projectName;
    private String title;
    private TaskStatus status;
    private Long assigneeId;
    private String assigneeName; 
    private Boolean isActive;
}