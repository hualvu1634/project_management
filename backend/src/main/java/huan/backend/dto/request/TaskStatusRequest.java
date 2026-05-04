package huan.backend.dto.request;

import huan.backend.enumerate.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusRequest {
    private TaskStatus status;
    private Long currentUserId;
}
