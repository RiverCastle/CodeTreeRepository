package com.codetree.CodeTreeHRM.system.mapper;

import com.codetree.CodeTreeHRM.system.dto.EmpRoleDto;
import com.codetree.CodeTreeHRM.system.dto.SystemRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemRoleMapper {

    /* ── 권한 마스터 (com_role) ── */
    List<SystemRoleDto> findAllRoles();
    SystemRoleDto findByRoleCd(String roleCd);
    int countByRoleCd(String roleCd);
    void insertRole(SystemRoleDto dto);
    void updateRole(SystemRoleDto dto);
    void updateRoleStatus(@Param("roleCd") String roleCd, @Param("useYn") String useYn);

    /* ── 권한별 사원 조회 (com_emp_role + hrm_emp_master + com_user) ── */
    List<EmpRoleDto> findEmpsByRoleCd(String roleCd);

    /* ── 특정 사원의 권한 목록 조회 ── */
    List<EmpRoleDto> findRolesByEmpNo(String empNo);

    /* ── 시스템 계정이 있는 사원 전체 조회 (권한 할당 대상) ── */
    List<EmpRoleDto> findAllUsableEmps();

    /* ── 권한 할당/해제 ── */
    void deleteEmpRoles(@Param("empNo") String empNo);
    void insertEmpRole(@Param("empNo") String empNo,
                       @Param("roleCd") String roleCd,
                       @Param("grntEmpNo") String grntEmpNo);
}
