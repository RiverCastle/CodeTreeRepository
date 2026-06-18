package com.codetree.CodeTreeHRM.message.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 공통 메시지(com_message) 단건 DTO.
 * PK 는 (msgId, langCd) 복합키.
 */
@Getter
@Setter
public class MessageDto {
    private String msgId;       // 메시지 코드 (PK)
    private String langCd;      // 언어 코드 (PK) - ko/en/ja
    private String msgType;     // 유형 - INFO/WARN/ERROR/CONFIRM
    private String msgContent;  // 메시지 본문 (MessageFormat 패턴: {0}, {1} ...)
    private String description; // 설명 (선택)
    private String isUse;       // 사용여부 Y/N
    private String createdAt;
    private String updatedAt;
}
