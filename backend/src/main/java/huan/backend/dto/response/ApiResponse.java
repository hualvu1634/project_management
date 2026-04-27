package huan.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import huan.backend.exception.ErrorField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private LocalDateTime timestamp;
    private Integer code;
    private String message;
    private List<ErrorField> details;
}
