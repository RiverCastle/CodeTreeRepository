package com.codetree.CodeTreeHRM.controller;

import com.codetree.CodeTreeHRM.entity.ComMenu;
import com.codetree.CodeTreeHRM.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<Map<String, Object>> getMenuTree() {
        return menuService.getMenuTree();
    }

    @GetMapping("/first-depth")
    public List<ComMenu> getFirstDepthMenus() {
        return menuService.getFirstDepthMenus();
    }

    @PostMapping
    public Map<String, Object> createMenu(@RequestBody ComMenu menu) {
        try {
            menuService.saveMenu(menu);
            return Map.of("success", true);
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @PutMapping("/{menuCd}")
    public Map<String, Object> updateMenu(@PathVariable String menuCd, @RequestBody ComMenu menu) {
        try {
            menuService.updateMenu(menuCd, menu);
            return Map.of("success", true);
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @PutMapping("/order")
    public Map<String, Object> updateMenuOrder(@RequestBody List<Map<String, Object>> orders) {
        try {
            menuService.updateMenuOrders(orders);
            return Map.of("success", true);
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @DeleteMapping("/{menuCd}")
    public Map<String, Object> deleteMenu(@PathVariable String menuCd) {
        try {
            menuService.deleteMenu(menuCd);
            return Map.of("success", true);
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }
}