package huan.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import huan.backend.dto.response.ErrorResponse;
import huan.backend.enums.ErrorCode;


import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, int code) {
        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .code(code) 
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.valueOf(code));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return buildErrorResponse(errorCode.getMessage(), errorCode.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorField>> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorField> details = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorCodeKey = error.getDefaultMessage();
                    ErrorCode code = ErrorCode.valueOf(errorCodeKey);
                    return new ErrorField(fieldName, fieldName + " " + code.getMessage());
                })
                .toList();

        return  new ResponseEntity<>(details,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorCode errorCode = ErrorCode.AUTH_FAILED;
        return buildErrorResponse(errorCode.getMessage(), errorCode.getCode());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException exception) {
        ErrorCode errorCode = ErrorCode.ACCOUNT_LOCKED;
        return buildErrorResponse(errorCode.getMessage(), errorCode.getCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnwantedException(Exception ex) {
        ex.printStackTrace();
        return buildErrorResponse("Lỗi hệ thống không xác định: " + ex.getMessage(), 500);
    }
}