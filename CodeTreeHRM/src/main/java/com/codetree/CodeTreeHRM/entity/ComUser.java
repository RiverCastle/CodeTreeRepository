package com.codetree.CodeTreeHRM.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "com_user")
@Getter
@NoArgsConstructor
public class ComUser {

    @Id
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "EMP_NO", nullable = false)
    private String empNo;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "USER_PWD", nullable = false)
    private String userPwd;

    @Column(name = "PWD_CHNG_DT")
    private LocalDateTime pwdChngDt;

    @Column(name = "PWD_EXPIR_DT")
    private LocalDateTime pwdExpirDt;

    @Column(name = "LOGIN_FAIL_ATMT_CNT", nullable = false)
    private Integer loginFailAtmtCnt;

    @Column(name = "ACCT_LOCK_YN", nullable = false)
    private String acctLockYn;

    @Column(name = "ACCT_LOCK_DT")
    private LocalDateTime acctLockDt;

    @Column(name = "LAST_LOGIN_DT")
    private LocalDateTime lastLoginDt;

    @Column(name = "USE_YN", nullable = false)
    private String useYn;

    @Column(name = "RGSTR_NO", nullable = false)
    private String rgstrNo;

    @Column(name = "REGIST_DT", insertable = false, updatable = false)
    private LocalDateTime registDt;

    @Column(name = "UPDT_DT", insertable = false, updatable = false)
    private LocalDateTime updtDt;
}
