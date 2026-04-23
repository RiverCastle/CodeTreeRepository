package com.codetree.CodeTreeHRM.menu.controller;

import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import com.codetree.CodeTreeHRM.menu.dto.MenuNode;
import com.codetree.CodeTreeHRM.menu.service.MenuService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/menu")
    public ResponseEntity<ApiResponse<List<MenuNode>>> getMenu(Authentication authentication) {
        Claims claims = (Claims) authentication.getDetails();
        int roleLevel = claims.get("roleLevel", Integer.class);
        List<MenuNode> tree = menuService.getMenuTree(roleLevel);
        return ResponseEntity.ok(ApiResponse.success(tree));
    }
}
