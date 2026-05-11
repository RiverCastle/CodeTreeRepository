package com.codetree.CodeTreeHRM.system.mapper;

import com.codetree.CodeTreeHRM.system.dto.UserDto;
import com.codetree.CodeTreeHRM.system.dto.UserSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /** 사용자 목록 (검색 + 페이징) */
    List<UserDto> searchUsers(UserSearchDto search);

    /** 목록 총 건수 */
    long countUsers(UserSearchDto search);

    /** 단건 상세 조회 */
    UserDto findByUserId(@Param("userId") String userId);

    /** userId 중복 체크 */
    int countByUserId(@Param("userId") String userId);

    /** empNo 유효성 체크 (hrm_emp_master 존재 여부) */
    int countEmpByEmpNo(@Param("empNo") String empNo);

    /** 신규 등록 */
    void insertUser(UserDto user);

    /** 정보 수정 (useYn, acctLockYn) */
    void updateUser(UserDto user);

    /** 비밀번호 변경 */
    void updatePassword(@Param("userId") String userId, @Param("encodedPwd") String encodedPwd);

    /** 계정 잠금 해제 (관리자용) */
    void unlockAccount(@Param("userId") String userId);
}
