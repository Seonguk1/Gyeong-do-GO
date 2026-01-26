package com.project.gyeong_do_go.global.exception;

import com.project.gyeong_do_go.global.dto.ApiResponse;
import com.project.gyeong_do_go.global.dto.ApiResponse.ErrorDetail;
import com.project.gyeong_do_go.global.error.ApiException;
import com.project.gyeong_do_go.global.error.ErrorCode;
import com.project.gyeong_do_go.global.error.ErrorReason;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApi(ApiException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.fail(ex.getCode()));
    }

    // Validation 어노테이션들로 에러가 났을 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toErrorDetail)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorCode.BAD_REQUEST, details));
    }

    private ErrorDetail toErrorDetail(FieldError fe) {
        String field = fe.getField();
        String msg = fe.getDefaultMessage();
        ErrorReason reason;
        try { reason = ErrorReason.valueOf(msg); }
        catch (Exception e) { reason = ErrorReason.UNEXPECTED; }
        return new ErrorDetail(field, reason);
    }

    // Enum 값이 잘못 들어왔을 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(HttpMessageNotReadableException ex) {
//        System.out.println(ex.getCause());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}