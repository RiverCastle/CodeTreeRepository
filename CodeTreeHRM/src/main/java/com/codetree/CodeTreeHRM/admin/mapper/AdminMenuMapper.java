package com.codetree.CodeTreeHRM.admin.mapper;

import com.codetree.CodeTreeHRM.admin.dto.MenuDto;
import com.codetree.CodeTreeHRM.admin.dto.MenuSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMenuMapper {
    List<MenuDto> searchMenus(@Param("search") MenuSearchDto search);
    int countByMenuCd(@Param("menuCd") String menuCd);
    int countChildMenus(@Param("menuCd") String menuCd);
    void insertMenu(MenuDto menu);
    void updateMenu(MenuDto menu);
    void deleteMenu(@Param("menuCd") String menuCd);
    List<MenuDto> findAllForSelect();
    int getMaxChildSequence(@Param("upperMenuCd") String upperMenuCd);
}
