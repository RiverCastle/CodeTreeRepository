package com.codetree.CodeTreeHRM.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonCodeDto {
    private String grpCd;
    private String grpNm;
    private String dc;
    private String useYn;
    private String registDt;
    private String updtDt;

    /** 상세 코드 목록 (단건 조회 시 포함) */
    private List<CommonCodeDtlDto> dtlList;
}
