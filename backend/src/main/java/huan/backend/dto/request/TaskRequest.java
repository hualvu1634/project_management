package huan.backend.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    private Long projectId;
    private String title;
    private Long assigneeId;
}