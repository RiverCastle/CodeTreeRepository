package com.codetree.CodeTreeHRM.service;

import com.codetree.CodeTreeHRM.entity.ComMenu;
import com.codetree.CodeTreeHRM.repository.ComMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final ComMenuRepository comMenuRepository;

    public List<Map<String, Object>> getMenuTree() {
        List<ComMenu> roots = comMenuRepository.findByUpperMenuCdIsNullOrderByMenuOrdr();
        List<Map<String, Object>> tree = new ArrayList<>();
        for (ComMenu root : roots) {
            Map<String, Object> node = toMap(root);
            List<ComMenu> children = comMenuRepository.findByUpperMenuCdOrderByMenuOrdr(root.getMenuCd());
            node.put("children", children.stream().map(this::toMap).toList());
            tree.add(node);
        }
        return tree;
    }

    public List<ComMenu> getFirstDepthMenus() {
        return comMenuRepository.findByUpperMenuCdIsNullOrderByMenuOrdr();
    }

    @Transactional
    public void saveMenu(ComMenu menu) {
        if (comMenuRepository.existsById(menu.getMenuCd())) {
            throw new IllegalArgumentException("이미 존재하는 메뉴코드입니다: " + menu.getMenuCd());
        }
        comMenuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(String menuCd, ComMenu updated) {
        ComMenu menu = comMenuRepository.findById(menuCd)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
        menu.setMenuNm(updated.getMenuNm());
        menu.setMenuUrl(updated.getMenuUrl());
        menu.setMenuOrdr(updated.getMenuOrdr());
        menu.setUseYn(updated.getUseYn());
    }

    @Transactional
    public void updateMenuOrders(List<Map<String, Object>> orders) {
        for (Map<String, Object> item : orders) {
            String menuCd = (String) item.get("menuCd");
            int menuOrdr = ((Number) item.get("menuOrdr")).intValue();
            comMenuRepository.findById(menuCd).ifPresent(m -> m.setMenuOrdr(menuOrdr));
        }
    }

    @Transactional
    public void deleteMenu(String menuCd) {
        if (comMenuRepository.existsByUpperMenuCd(menuCd)) {
            throw new IllegalStateException("하위 메뉴가 존재하여 삭제할 수 없습니다.");
        }
        comMenuRepository.deleteById(menuCd);
    }

    private Map<String, Object> toMap(ComMenu m) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("menuCd", m.getMenuCd());
        map.put("menuNm", m.getMenuNm());
        map.put("upperMenuCd", m.getUpperMenuCd());
        map.put("menuUrl", m.getMenuUrl() != null ? m.getMenuUrl() : "");
        map.put("menuOrdr", m.getMenuOrdr());
        map.put("useYn", m.getUseYn());
        return map;
    }
}