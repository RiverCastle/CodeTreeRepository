package com.codetree.CodeTreeHRM.hr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
    private String empNo;
    private String empNm;
    private String empEngNm;
    private String emlAddr;
    private String deptCd;
    private String deptNm;
    private String jbgdCd;
    private String jbgdNm;
    private String empSttsCd;
    private String emplSeCd;
    private String jncmpYmd;
    private String retirYmd;
    /** 요청/응답 시 평문, DB에는 암호화 저장 */
    private String rsdntNo;
    private String brdt;
    private String sexdstnCd;
    private String mbtlnum;
    private String addr;
    private String zipCd;
    private String addrDtl;
    private String bankCd;
    private String actno;
    private String emrgncCntctNm;
    private String emrgncTelno;
    private Long fileRefId;
    private String registDt;
    private String updtDt;
}
