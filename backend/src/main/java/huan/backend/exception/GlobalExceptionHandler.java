package huan.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import huan.backend.dto.response.ApiResponse;
import huan.backend.enumerate.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse> buildErrorResponse(String message, int code, List<ErrorField> details) {
        ApiResponse response = ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .code(code)
                .details(details) 
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.valueOf(code));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return buildErrorResponse(errorCode.getMessage(), errorCode.getCode(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorField> details = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorCodeKey = error.getDefaultMessage();
                    ErrorCode code = ErrorCode.valueOf(errorCodeKey);
                    return new ErrorField(fieldName, fieldName + " " + code.getMessage());
                })
                .toList();

        return buildErrorResponse("Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST.value(), details);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorCode errorCode = ErrorCode.AUTH_FAILED;
        return buildErrorResponse(errorCode.getMessage(), errorCode.getCode(), null);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse> handleDisabledException(DisabledException exception) {
        ErrorCode errorCode = ErrorCode.ACCOUNT_LOCKED;
        return buildErrorResponse(errorCode.getMessage(), errorCode.getCode(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnwantedException(Exception ex) {
        ex.printStackTrace();
        return buildErrorResponse("Lỗi hệ thống không xác định: " + ex.getMessage(), 500, null);
    }
}