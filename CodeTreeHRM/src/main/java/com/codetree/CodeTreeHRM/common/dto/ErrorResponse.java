package com.codetree.CodeTreeHRM.common.dto;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * CustomException 글로벌 처리 시 프론트엔드로 내려가는 표준 에러 응답.
 * <pre>
 * {
 *   "success": false,
 *   "code": "ERR_DUPLICATE",
 *   "message": "이미 등록된 20260604입니다. 다른 값을 입력해 주세요.",
 *   "timestamp": "2026-06-18T20:50:00"
 * }
 * </pre>
 */
@Getter
public class ErrorResponse {

    private final boolean success = false;
    private final String code;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
