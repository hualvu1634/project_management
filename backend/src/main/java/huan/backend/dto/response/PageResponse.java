package huan.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class PageResponse<T>  implements Serializable{
    private int current; 
    private int total;  
    private int size;    
    private long totalElements; 
    private List<T> data;    
}