package huan.backend.dto.response;

import java.time.LocalDate;

import huan.backend.enums.Priority;
import huan.backend.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskResponse {
    private String title;
    private TaskStatus status;
    private Priority priority;
    private String assigneeName; 
    private LocalDate createDate;
}