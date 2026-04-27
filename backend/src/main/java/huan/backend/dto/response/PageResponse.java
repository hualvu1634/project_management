package huan.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class PageResponse<T> {
    private int currentPage; // Trang hiện tại
    private int totalPages;  // Tổng số trang
    private int pageSize;    // Số lượng phần tử trong trang
    private long totalElements; // Tổng số phần tử
    private List<T> data;    // Dữ liệu
}