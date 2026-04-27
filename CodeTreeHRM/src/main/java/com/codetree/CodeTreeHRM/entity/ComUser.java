package com.codetree.CodeTreeHRM.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "com_user")
@Getter
@NoArgsConstructor
public class ComUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "EMP_NO", nullable = false, unique = true, length = 20)
    private String empNo;

    @Column(name = "USER_ID", nullable = false, unique = true, length = 100)
    private String userId;

    @Column(name = "USER_PWD", nullable = false, length = 255)
    private String userPwd;

    @Column(name = "PWD_CHNG_DT")
    private LocalDateTime pwdChngDt;

    @Column(name = "PWD_EXPIR_DT")
    private LocalDateTime pwdExpirDt;

    @Column(name = "LOGIN_FAIL_ATMT_CNT", nullable = false)
    private int loginFailAtmtCnt;

    @Column(name = "ACCT_LOCK_YN", nullable = false, length = 1)
    private String acctLockYn;

    @Column(name = "ACCT_LOCK_DT")
    private LocalDateTime acctLockDt;

    @Column(name = "LAST_LOGIN_DT")
    private LocalDateTime lastLoginDt;

    @Column(name = "USE_YN", nullable = false, length = 1)
    private String useYn;

    @Column(name = "RGSTR_NO", nullable = false, length = 20)
    private String rgstrNo;

    @Column(name = "REGIST_DT", nullable = false, updatable = false)
    private LocalDateTime registDt;

    @Column(name = "UPDT_DT", nullable = false)
    private LocalDateTime updtDt;

    public void recordLoginSuccess() {
        this.loginFailAtmtCnt = 0;
        this.lastLoginDt = LocalDateTime.now();
    }

    public void recordLoginFailure() {
        this.loginFailAtmtCnt++;
        if (this.loginFailAtmtCnt >= 5) {
            this.acctLockYn = "Y";
            this.acctLockDt = LocalDateTime.now();
        }
    }
}