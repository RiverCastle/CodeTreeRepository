package com.codetree.CodeTreeHRM.hr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeSearchDto {
    private String empNm;
    private String deptCd;
    private String jbgdCd;
    private String empSttsCd;
    private int page = 1;
    private int size = 20;

    public int getOffset() {
        return (page - 1) * size;
    }
}
