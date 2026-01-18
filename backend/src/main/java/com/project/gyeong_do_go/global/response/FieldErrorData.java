// File: src/main/java/com/project/gyeong_do_go/global/response/FieldErrorData.java
package com.project.gyeong_do_go.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "유효성 검증 상세 에러")
public class FieldErrorData {

    @Schema(example = "capacity")
    private String field;

    @Schema(example = "must be greater than or equal to 2")
    private String message;
}
