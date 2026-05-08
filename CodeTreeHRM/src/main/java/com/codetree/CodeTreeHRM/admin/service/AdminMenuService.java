package com.codetree.CodeTreeHRM.admin.service;

import com.codetree.CodeTreeHRM.admin.dto.MenuDto;
import com.codetree.CodeTreeHRM.admin.dto.MenuSearchDto;
import com.codetree.CodeTreeHRM.admin.mapper.AdminMenuMapper;
import com.codetree.CodeTreeHRM.admin.mapper.AdminRoleMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMenuService {

    private final AdminMenuMapper adminMenuMapper;
    private final AdminRoleMenuMapper adminRoleMenuMapper;

    @Transactional(readOnly = true)
    public List<MenuDto> searchMenus(MenuSearchDto search) {
        return adminMenuMapper.searchMenus(search);
    }

    @Transactional(readOnly = true)
    public List<MenuDto> findAllForSelect() {
        return adminMenuMapper.findAllForSelect();
    }

    @Transactional(readOnly = true)
    public List<String> findAssignedRoleCds(String menuCd) {
        return adminRoleMenuMapper.findAssignedRoleCdsByMenuCd(menuCd);
    }

    @Transactional
    public void createMenu(MenuDto menu) {
        if (adminMenuMapper.countByMenuCd(menu.getMenuCd()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 메뉴코드입니다: " + menu.getMenuCd());
        }
        adminMenuMapper.insertMenu(menu);
        saveRoleAssignments(menu.getMenuCd(), menu.getRoleCdList());
    }

    @Transactional
    public void updateMenu(String menuCd, MenuDto menu) {
        menu.setMenuCd(menuCd);
        adminMenuMapper.updateMenu(menu);
        saveRoleAssignments(menuCd, menu.getRoleCdList());
    }

    @Transactional(readOnly = true)
    public String getNextMenuCd(String upperMenuCd) {
        if (upperMenuCd == null || upperMenuCd.isBlank()) {
            return "";
        }
        int nextSeq = adminMenuMapper.getMaxChildSequence(upperMenuCd) + 1;
        return String.format("%s-%02d", upperMenuCd, nextSeq);
    }

    @Transactional
    public void deleteMenu(String menuCd) {
        if (adminMenuMapper.countChildMenus(menuCd) > 0) {
            throw new IllegalArgumentException("하위 메뉴가 존재하여 삭제할 수 없습니다.");
        }
        adminRoleMenuMapper.deleteRoleMenusByMenuCd(menuCd);
        adminMenuMapper.deleteMenu(menuCd);
    }

    private void saveRoleAssignments(String menuCd, List<String> roleCdList) {
        adminRoleMenuMapper.deleteRoleMenusByMenuCd(menuCd);
        if (roleCdList != null && !roleCdList.isEmpty()) {
            for (String roleCd : roleCdList) {
                adminRoleMenuMapper.insertRoleMenu(roleCd, menuCd);
            }
        }
    }
}
