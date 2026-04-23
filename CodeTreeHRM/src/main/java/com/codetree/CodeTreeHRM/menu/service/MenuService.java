package com.codetree.CodeTreeHRM.menu.service;

import com.codetree.CodeTreeHRM.menu.dto.MenuNode;
import com.codetree.CodeTreeHRM.menu.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuMapper menuMapper;

    public List<MenuNode> getMenuTree(int roleLevel) {
        List<MenuNode> all = menuMapper.findAccessibleMenus(roleLevel);

        Map<String, MenuNode> byCode = all.stream()
                .collect(Collectors.toMap(MenuNode::getMenuCd, m -> m));

        return all.stream()
                .filter(m -> m.getUpperMenuCd() == null)
                .peek(root -> attachChildren(root, byCode, all))
                .collect(Collectors.toList());
    }

    private void attachChildren(MenuNode parent, Map<String, MenuNode> byCode, List<MenuNode> all) {
        all.stream()
                .filter(m -> parent.getMenuCd().equals(m.getUpperMenuCd()))
                .forEach(child -> {
                    parent.getChildren().add(child);
                    attachChildren(child, byCode, all);
                });
    }
}
