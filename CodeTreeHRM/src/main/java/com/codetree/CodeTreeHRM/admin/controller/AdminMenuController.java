package com.codetree.CodeTreeHRM.admin.controller;

import com.codetree.CodeTreeHRM.admin.dto.MenuDto;
import com.codetree.CodeTreeHRM.admin.dto.MenuSearchDto;
import com.codetree.CodeTreeHRM.admin.service.AdminMenuService;
import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menus")
@RequiredArgsConstructor
public class AdminMenuController {

    private final AdminMenuService adminMenuService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuDto>>> searchMenus(MenuSearchDto search) {
        return ResponseEntity.ok(ApiResponse.success(adminMenuService.searchMenus(search)));
    }

    @GetMapping("/parents")
    public ResponseEntity<ApiResponse<List<MenuDto>>> getParentMenus() {
        return ResponseEntity.ok(ApiResponse.success(adminMenuService.findAllForSelect()));
    }

    @GetMapping("/next-code")
    public ResponseEntity<ApiResponse<String>> getNextMenuCd(
            @RequestParam(required = false) String upperMenuCd) {
        return ResponseEntity.ok(ApiResponse.success(adminMenuService.getNextMenuCd(upperMenuCd)));
    }

    @GetMapping("/{menuCd}/roles")
    public ResponseEntity<ApiResponse<List<String>>> getAssignedRoles(@PathVariable String menuCd) {
        return ResponseEntity.ok(ApiResponse.success(adminMenuService.findAssignedRoleCds(menuCd)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMenu(@RequestBody MenuDto menu) {
        try {
            adminMenuService.createMenu(menu);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{menuCd}")
    public ResponseEntity<ApiResponse<Void>> updateMenu(
            @PathVariable String menuCd,
            @RequestBody MenuDto menu) {
        adminMenuService.updateMenu(menuCd, menu);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{menuCd}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable String menuCd) {
        try {
            adminMenuService.deleteMenu(menuCd);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
