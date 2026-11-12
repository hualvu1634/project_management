package huan.backend.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitRequest {
    private Long taskId;
    private Long id;
    private String filePath;
}
