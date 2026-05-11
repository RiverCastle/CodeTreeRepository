package com.codetree.CodeTreeHRM.hr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosDto {
    private String jbgdCd;
    private String jbgdNm;
    private Integer jbgdOrdr;
    private String useYn;
    private String registDt;
    private String updtDt;
    /** 해당 직급을 사용 중인 사원 수 (상태 변경 유효성 검사용, 읽기 전용) */
    private Integer empCount;
}
