package com.codetree.CodeTreeHRM.repository;

import com.codetree.CodeTreeHRM.entity.ComMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComMenuRepository extends JpaRepository<ComMenu, String> {
    List<ComMenu> findByUpperMenuCdIsNullOrderByMenuOrdr();
    List<ComMenu> findByUpperMenuCdOrderByMenuOrdr(String upperMenuCd);
    boolean existsByUpperMenuCd(String menuCd);
}