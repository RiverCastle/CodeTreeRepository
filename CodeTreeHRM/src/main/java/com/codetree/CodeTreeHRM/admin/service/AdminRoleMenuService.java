package com.codetree.CodeTreeHRM.admin.service;

import com.codetree.CodeTreeHRM.admin.dto.MenuDto;
import com.codetree.CodeTreeHRM.admin.dto.RoleDto;
import com.codetree.CodeTreeHRM.admin.mapper.AdminRoleMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRoleMenuService {

    private final AdminRoleMenuMapper adminRoleMenuMapper;

    @Transactional(readOnly = true)
    public List<RoleDto> findAllRoles() {
        return adminRoleMenuMapper.findAllRoles();
    }

    @Transactional(readOnly = true)
    public List<MenuDto> findAllMenus() {
        return adminRoleMenuMapper.findAllMenus();
    }

    @Transactional(readOnly = true)
    public List<String> findAssignedMenuCds(String roleCd) {
        return adminRoleMenuMapper.findAssignedMenuCds(roleCd);
    }

    @Transactional
    public void saveRoleMenus(String roleCd, List<String> menuCdList) {
        adminRoleMenuMapper.deleteRoleMenus(roleCd);
        if (menuCdList != null && !menuCdList.isEmpty()) {
            for (String menuCd : menuCdList) {
                adminRoleMenuMapper.insertRoleMenu(roleCd, menuCd);
            }
        }
    }
}
