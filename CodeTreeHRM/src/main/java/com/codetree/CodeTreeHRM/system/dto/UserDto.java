package com.codetree.CodeTreeHRM.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long   userNo;
    private String empNo;
    private String userId;
    /** 등록/비밀번호 변경 시에만 사용 (응답 시 null 반환) */
    private String userPwd;

    /** 조회용 조인 필드 */
    private String empNm;
    private String deptNm;

    private String  useYn;
    private String  acctLockYn;
    private Integer loginFailAtmtCnt;

    private String lastLoginDt;
    private String registDt;
    private String updtDt;
    private String rgstrNo;
}
