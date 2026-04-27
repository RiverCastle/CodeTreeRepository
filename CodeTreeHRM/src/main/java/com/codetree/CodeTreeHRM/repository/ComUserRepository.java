package com.codetree.CodeTreeHRM.repository;

import com.codetree.CodeTreeHRM.entity.ComUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ComUserRepository extends JpaRepository<ComUser, Long> {

    Optional<ComUser> findByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE com_user SET LOGIN_FAIL_ATMT_CNT = LOGIN_FAIL_ATMT_CNT + 1 WHERE USER_NO = :userNo", nativeQuery = true)
    void incrementLoginFailCount(@Param("userNo") Long userNo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE com_user SET ACCT_LOCK_YN = 'Y', ACCT_LOCK_DT = NOW() WHERE USER_NO = :userNo", nativeQuery = true)
    void lockAccount(@Param("userNo") Long userNo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE com_user SET LOGIN_FAIL_ATMT_CNT = 0, LAST_LOGIN_DT = NOW() WHERE USER_NO = :userNo", nativeQuery = true)
    void resetOnLoginSuccess(@Param("userNo") Long userNo);
}
