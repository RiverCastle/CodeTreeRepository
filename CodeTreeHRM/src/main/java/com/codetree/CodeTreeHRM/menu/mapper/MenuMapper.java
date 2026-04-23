package com.codetree.CodeTreeHRM.menu.mapper;

import com.codetree.CodeTreeHRM.menu.dto.MenuNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<MenuNode> findAccessibleMenus(@Param("roleLevel") int roleLevel);
}
