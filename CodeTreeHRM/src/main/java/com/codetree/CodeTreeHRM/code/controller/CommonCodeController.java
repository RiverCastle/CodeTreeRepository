package com.codetree.CodeTreeHRM.code.controller;

import com.codetree.CodeTreeHRM.code.dto.CommonCodeDtlDto;
import com.codetree.CodeTreeHRM.code.dto.CommonCodeDto;
import com.codetree.CodeTreeHRM.code.service.CommonCodeService;
import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common/codes")
@RequiredArgsConstructor
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    /* ── 그룹 코드 목록 ── */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommonCodeDto>>> getAllGrps(
            @RequestParam(required = false) String grpNm,
            @RequestParam(required = false) String useYn) {
        return ResponseEntity.ok(ApiResponse.success(commonCodeService.findAllGrps(grpNm, useYn)));
    }

    /* ── 그룹 코드 저장 (Upsert) ── */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveGrp(@RequestBody CommonCodeDto dto) {
        try {
            commonCodeService.saveGrp(dto);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /* ── 그룹 사용여부 변경 ── */
    @PatchMapping("/{grpCd}/status")
    public ResponseEntity<ApiResponse<Void>> updateGrpStatus(
            @PathVariable String grpCd,
            @RequestBody Map<String, String> body) {
        commonCodeService.updateGrpStatus(grpCd, body.get("useYn"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* ── 상세 코드 목록 ── */
    @GetMapping("/{grpCd}/details")
    public ResponseEntity<ApiResponse<List<CommonCodeDtlDto>>> getDtls(@PathVariable String grpCd) {
        return ResponseEntity.ok(ApiResponse.success(commonCodeService.findDtlsByGrpCd(grpCd)));
    }

    /* ── 상세 코드 저장 (Upsert) ── */
    @PostMapping("/{grpCd}/details")
    public ResponseEntity<ApiResponse<Void>> saveDtl(
            @PathVariable String grpCd,
            @RequestBody CommonCodeDtlDto dto) {
        try {
            commonCodeService.saveDtl(grpCd, dto);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /* ── 상세 코드 사용여부 변경 ── */
    @PatchMapping("/{grpCd}/details/{dtlCd}/status")
    public ResponseEntity<ApiResponse<Void>> updateDtlStatus(
            @PathVariable String grpCd,
            @PathVariable String dtlCd,
            @RequestBody Map<String, String> body) {
        commonCodeService.updateDtlStatus(grpCd, dtlCd, body.get("useYn"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* ── 상세 코드 삭제 ── */
    @DeleteMapping("/{grpCd}/details/{dtlCd}")
    public ResponseEntity<ApiResponse<Void>> deleteDtl(
            @PathVariable String grpCd,
            @PathVariable String dtlCd) {
        try {
            commonCodeService.deleteDtl(grpCd, dtlCd);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
