package huan.backend.dto.request;

import huan.backend.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusRequest {
    private Long taskId;
    private TaskStatus status;
    private Long userId;
}
