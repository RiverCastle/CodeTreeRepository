package com.codetree.CodeTreeHRM.admin.controller;

import com.codetree.CodeTreeHRM.admin.dto.RoleDto;
import com.codetree.CodeTreeHRM.admin.dto.RoleMenuSaveDto;
import com.codetree.CodeTreeHRM.admin.service.AdminRoleMenuService;
import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class AdminRoleMenuController {

    private final AdminRoleMenuService adminRoleMenuService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDto>>> findAllRoles() {
        return ResponseEntity.ok(ApiResponse.success(adminRoleMenuService.findAllRoles()));
    }

    @GetMapping("/{roleCd}/menus")
    public ResponseEntity<ApiResponse<List<String>>> findAssignedMenus(@PathVariable String roleCd) {
        return ResponseEntity.ok(ApiResponse.success(adminRoleMenuService.findAssignedMenuCds(roleCd)));
    }

    @PutMapping("/{roleCd}/menus")
    public ResponseEntity<ApiResponse<Void>> saveRoleMenus(
            @PathVariable String roleCd,
            @RequestBody RoleMenuSaveDto dto) {
        adminRoleMenuService.saveRoleMenus(roleCd, dto.getMenuCdList());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
