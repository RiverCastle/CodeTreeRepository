package com.codetree.CodeTreeHRM.hr.controller;

import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import com.codetree.CodeTreeHRM.hr.dto.DepartmentDto;
import com.codetree.CodeTreeHRM.hr.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /** 부서 트리 조회 - 계층 구조 포함 전체 목록 */
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getDeptTree() {
        return ResponseEntity.ok(ApiResponse.success(departmentService.getDeptTree()));
    }

    /** 부서 상세 조회 */
    @GetMapping("/{deptCd}")
    public ResponseEntity<ApiResponse<DepartmentDto>> getDept(@PathVariable String deptCd) {
        DepartmentDto dept = departmentService.findByDeptCd(deptCd);
        if (dept == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ApiResponse.success(dept));
    }

    /** 부서 저장/수정 (Upsert) */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveDept(@RequestBody DepartmentDto dto) {
        try {
            departmentService.saveDepartment(dto);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** 부서 폐지 처리: USE_YN = 'N', CLSG_YMD = 오늘 */
    @PatchMapping("/{deptCd}/close")
    public ResponseEntity<ApiResponse<Void>> closeDept(@PathVariable String deptCd) {
        try {
            departmentService.closeDepartment(deptCd);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
