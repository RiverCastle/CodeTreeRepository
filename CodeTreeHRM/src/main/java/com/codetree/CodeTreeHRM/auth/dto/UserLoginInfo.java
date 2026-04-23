package com.codetree.CodeTreeHRM.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginInfo {
    private Long userNo;
    private String empNo;
    private String userId;
    private String userPwd;
    private int loginFailAtmtCnt;
    private String acctLockYn;
    private String useYn;
    private String empSttsCd;
    private String empNm;
}
