package com.codetree.CodeTreeHRM.hr.controller;

import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import com.codetree.CodeTreeHRM.hr.dto.PosDto;
import com.codetree.CodeTreeHRM.hr.service.PosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hr/positions")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;

    /** 직급 전체 목록 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PosDto>>> getPositions() {
        return ResponseEntity.ok(ApiResponse.success(posService.findAll()));
    }

    /** 직급 단건 조회 */
    @GetMapping("/{jbgdCd}")
    public ResponseEntity<ApiResponse<PosDto>> getPosition(@PathVariable String jbgdCd) {
        PosDto pos = posService.findByJbgdCd(jbgdCd);
        if (pos == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(pos));
    }

    /** 직급 저장/수정 (Upsert) */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> savePosition(@RequestBody PosDto pos) {
        try {
            posService.savePos(pos);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** 직급 사용 여부 변경 */
    @PatchMapping("/{jbgdCd}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable String jbgdCd,
            @RequestBody Map<String, String> body) {
        try {
            String useYn = body.get("useYn");
            if (useYn == null || (!useYn.equals("Y") && !useYn.equals("N"))) {
                return ResponseEntity.badRequest().body(ApiResponse.error("useYn 값은 'Y' 또는 'N' 이어야 합니다."));
            }
            posService.updateStatus(jbgdCd, useYn);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
