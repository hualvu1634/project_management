package huan.backend.dto.response;

import huan.backend.enumerate.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskResponse {

    private String title;
    private TaskStatus status;
    private String assigneeName; 
}