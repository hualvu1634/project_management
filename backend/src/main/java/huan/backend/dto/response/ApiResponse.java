package huan.backend.dto.response;

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
    private String message;
    private List<ErrorField> details;
}
