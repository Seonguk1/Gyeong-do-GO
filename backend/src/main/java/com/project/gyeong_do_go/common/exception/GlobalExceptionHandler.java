package com.project.gyeong_do_go.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 모든 컨트롤러에서 발생하는 에러를 여기서 잡겠다.
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    // 직접 던진 비즈니스 에러 처리
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(
                        errorCode.getStatus(),
                        errorCode.getCode(),
                        errorCode.getMessage(),
                        null
                ));
    }

    // @Valid 검증 실패 시 발생하는 에러 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> details = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            // 핵심: getDefaultMessage() 대신 messageSource를 사용한다.
            // error 객체 자체를 넘겨야 {0}, {1} 등 인자가 자동으로 매핑된다.
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            details.put(error.getField(), message);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(400, "COMMON_001", "입력값이 올바르지 않습니다.", details));
    }
}
