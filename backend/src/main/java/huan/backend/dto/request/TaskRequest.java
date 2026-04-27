package huan.backend.dto.request;


import huan.backend.enumerate.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    private Long projectId;
    private String title;
    private TaskStatus status;

    private Long assigneeId;
}