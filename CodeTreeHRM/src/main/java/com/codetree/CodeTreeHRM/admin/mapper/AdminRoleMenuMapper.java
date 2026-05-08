package com.codetree.CodeTreeHRM.admin.mapper;

import com.codetree.CodeTreeHRM.admin.dto.MenuDto;
import com.codetree.CodeTreeHRM.admin.dto.RoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminRoleMenuMapper {
    List<RoleDto> findAllRoles();
    List<MenuDto> findAllMenus();
    List<String> findAssignedMenuCds(@Param("roleCd") String roleCd);
    void deleteRoleMenus(@Param("roleCd") String roleCd);
    void insertRoleMenu(@Param("roleCd") String roleCd, @Param("menuCd") String menuCd);
    List<String> findAssignedRoleCdsByMenuCd(@Param("menuCd") String menuCd);
    void deleteRoleMenusByMenuCd(@Param("menuCd") String menuCd);
}
