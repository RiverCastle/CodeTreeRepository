package com.codetree.CodeTreeHRM.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuNode {
    private String menuCd;
    private String menuNm;
    private String upperMenuCd;
    private String menuUrl;
    private int menuOrdr;
    private List<MenuNode> children = new ArrayList<>();
}
