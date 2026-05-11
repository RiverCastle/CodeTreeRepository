package com.codetree.CodeTreeHRM.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemRoleDto {
    private String roleCd;
    private String roleNm;
    private Integer roleLevel;
    private String dc;
    private String useYn;
    private String registDt;
    private String updtDt;
    private int empCount;   // 해당 권한을 보유한 사원 수
}
