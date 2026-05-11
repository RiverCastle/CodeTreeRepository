package com.codetree.CodeTreeHRM.system.controller;

import com.codetree.CodeTreeHRM.system.dto.EmpRoleDto;
import com.codetree.CodeTreeHRM.system.dto.RoleAssignDto;
import com.codetree.CodeTreeHRM.system.dto.SystemRoleDto;
import com.codetree.CodeTreeHRM.system.service.SystemRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class SystemRoleController {

    private final SystemRoleService systemRoleService;

    /* ── 권한 전체 목록 ── */
    @GetMapping
    public ResponseEntity<List<SystemRoleDto>> findAllRoles() {
        return ResponseEntity.ok(systemRoleService.findAllRoles());
    }

    /* ── 권한 단건 조회 ── */
    @GetMapping("/{roleCd}")
    public ResponseEntity<SystemRoleDto> findByRoleCd(@PathVariable String roleCd) {
        SystemRoleDto dto = systemRoleService.findByRoleCd(roleCd);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    /* ── 권한 저장 (Upsert) ── */
    @PostMapping
    public ResponseEntity<Void> saveRole(@RequestBody SystemRoleDto dto) {
        systemRoleService.saveRole(dto);
        return ResponseEntity.ok().build();
    }

    /* ── 권한 상태 토글 (USE_YN) ── */
    @PatchMapping("/{roleCd}/status")
    public ResponseEntity<Void> toggleRoleStatus(
            @PathVariable String roleCd,
            @RequestBody Map<String, String> body) {
        String useYn = body.get("useYn");
        systemRoleService.toggleRoleStatus(roleCd, useYn);
        return ResponseEntity.ok().build();
    }

    /* ── 특정 권한을 보유한 사원 목록 ── */
    @GetMapping("/{roleCd}/emps")
    public ResponseEntity<List<EmpRoleDto>> findEmpsByRoleCd(@PathVariable String roleCd) {
        return ResponseEntity.ok(systemRoleService.findEmpsByRoleCd(roleCd));
    }

    /* ── 특정 사원의 권한 목록 ── */
    @GetMapping("/emp/{empNo}")
    public ResponseEntity<List<EmpRoleDto>> findRolesByEmpNo(@PathVariable String empNo) {
        return ResponseEntity.ok(systemRoleService.findRolesByEmpNo(empNo));
    }

    /* ── 권한 할당 대상 사원 목록 (계정 있는 재직자) ── */
    @GetMapping("/usable-emps")
    public ResponseEntity<List<EmpRoleDto>> findAllUsableEmps() {
        return ResponseEntity.ok(systemRoleService.findAllUsableEmps());
    }

    /* ── 권한 할당 저장 ── */
    @PostMapping("/assign")
    public ResponseEntity<Void> assignRoles(@RequestBody RoleAssignDto dto) {
        systemRoleService.assignRoles(dto);
        return ResponseEntity.ok().build();
    }
}
