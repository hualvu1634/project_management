package huan.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailResponse {
    private String to;
    private String subject;
    private String text;
}
