package com.codetree.CodeTreeHRM.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpRoleDto {
    // 사원 정보 (hrm_emp_master)
    private String empNo;
    private String empNm;
    private String deptCd;
    private String deptNm;
    private String jbgdNm;
    private String empSttsCd;
    // 계정 정보 (com_user)
    private String userId;
    private String acctLockYn;
    private String userUseYn;
    // 권한 매핑 정보 (com_emp_role)
    private String roleCd;
    private String roleNm;
    private String grntDt;
    private String grntEmpNo;
    private Long empRoleNo;
}
