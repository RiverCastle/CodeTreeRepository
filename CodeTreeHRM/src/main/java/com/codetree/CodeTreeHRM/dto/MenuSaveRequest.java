package com.codetree.CodeTreeHRM.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuSaveRequest {
    private String menuCd;
    private String menuNm;
    private String menuIcon;
    private String upperMenuCd;
    private String menuUrl;
    private Integer menuOrdr;
    private String useYn;
}
