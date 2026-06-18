package com.codetree.CodeTreeHRM.message.controller;

import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import com.codetree.CodeTreeHRM.message.dto.MessageDto;
import com.codetree.CodeTreeHRM.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /* ── 목록 조회 (검색: 코드/본문 keyword, 언어, 유형) ── */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MessageDto>>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String langCd,
            @RequestParam(required = false) String msgType) {
        return ResponseEntity.ok(ApiResponse.success(messageService.findAll(keyword, langCd, msgType)));
    }

    /* ── 프론트엔드 메시지 카탈로그 (언어별 msgId→content 맵) ── */
    @GetMapping("/catalog")
    public ResponseEntity<ApiResponse<Map<String, String>>> catalog(
            @RequestParam(required = false, defaultValue = MessageService.DEFAULT_LANG) String langCd) {
        return ResponseEntity.ok(ApiResponse.success(messageService.getCatalog(langCd)));
    }

    /* ── 등록/수정 (Upsert) ── */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(@RequestBody MessageDto dto) {
        messageService.save(dto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* ── 사용여부 변경 ── */
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@RequestBody Map<String, String> body) {
        messageService.updateStatus(body.get("msgId"), body.get("langCd"), body.get("isUse"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* ── 삭제 ── */
    @DeleteMapping("/{msgId}/{langCd}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String msgId,
                                                    @PathVariable String langCd) {
        messageService.delete(msgId, langCd);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* ── 캐시 새로고침 (운영 중 실시간 반영) ── */
    @PostMapping("/reload-cache")
    public ResponseEntity<ApiResponse<Void>> reloadCache() {
        messageService.reloadCache();
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
