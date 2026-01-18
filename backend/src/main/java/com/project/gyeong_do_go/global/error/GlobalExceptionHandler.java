// File: src/main/java/com/project/gyeong_do_go/global/error/GlobalExceptionHandler.java
package com.project.gyeong_do_go.global.error;

import com.project.gyeong_do_go.global.response.ApiResponse;
import com.project.gyeong_do_go.global.response.FieldErrorData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e, HttpServletRequest req) {
        ErrorCode ec = e.getErrorCode();
        log.warn("BusinessException code={}, path={}, msg={}", ec.getCode(), req.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(ec.getHttpStatus())
                .body(ApiResponse.error(ec.getCode(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest req) {
        log.warn("IllegalArgument path={}, msg={}", req.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        List<FieldErrorData> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldErrorData(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        log.warn("Validation failed path={}, details={}", req.getRequestURI(), details.size());

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), "Invalid request", details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException e, HttpServletRequest req) {
        log.warn("ConstraintViolation path={}, msg={}", req.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), "Invalid request"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException e, HttpServletRequest req) {
        log.error("DataIntegrityViolation path={}, msg={}", req.getRequestURI(), e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.INTERNAL_ERROR.getCode(), "Database constraint violated"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception e, HttpServletRequest req) {
        log.error("Unhandled exception path={}", req.getRequestURI(), e);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.INTERNAL_ERROR.getCode(), "Internal server error"));
    }
}
