package com.codetree.CodeTreeHRM.hr.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DepartmentDto {
    private String deptCd;
    private String upperDeptCd;
    private String upperDeptNm;
    private Integer deptLvl;
    private Integer deptOrdr;
    private String deptNm;
    private String deptEngNm;
    private String deptShrtNm;
    private String deptSeCd;
    private String deptMngrNo;
    private String deptMngrNm;
    private String deptTelno;
    private String deptLctnNm;
    private String estblYmd;
    private String clsgYmd;
    private String useYn;
    private String registDt;
    private String updtDt;
    private List<DepartmentDto> children;
}
