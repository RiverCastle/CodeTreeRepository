package com.codetree.CodeTreeHRM.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuDto {
    private String menuCd;
    private String menuNm;
    private String upperMenuCd;
    private String upperMenuNm;
    private String menuUrl;
    private Integer menuOrdr;
    private String useYn;
    private String crtnDt;
    private String updtDt;
    /** 등록/수정 시 매핑할 권한 코드 목록 (입력 전용) */
    private List<String> roleCdList;
}
