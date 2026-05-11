package com.codetree.CodeTreeHRM.system.service;

import com.codetree.CodeTreeHRM.system.dto.EmpRoleDto;
import com.codetree.CodeTreeHRM.system.dto.RoleAssignDto;
import com.codetree.CodeTreeHRM.system.dto.SystemRoleDto;
import com.codetree.CodeTreeHRM.system.mapper.SystemRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemRoleService {

    private final SystemRoleMapper systemRoleMapper;

    /* ── 권한 전체 목록 ── */
    @Transactional(readOnly = true)
    public List<SystemRoleDto> findAllRoles() {
        return systemRoleMapper.findAllRoles();
    }

    /* ── 권한 단건 조회 ── */
    @Transactional(readOnly = true)
    public SystemRoleDto findByRoleCd(String roleCd) {
        return systemRoleMapper.findByRoleCd(roleCd);
    }

    /* ── 권한 저장 (Upsert) ── */
    @Transactional
    public void saveRole(SystemRoleDto dto) {
        validateRole(dto);
        boolean exists = systemRoleMapper.countByRoleCd(dto.getRoleCd()) > 0;
        if (exists) {
            systemRoleMapper.updateRole(dto);
        } else {
            systemRoleMapper.insertRole(dto);
        }
    }

    /* ── 권한 상태 토글 (USE_YN) ── */
    @Transactional
    public void toggleRoleStatus(String roleCd, String useYn) {
        if (!"Y".equals(useYn) && !"N".equals(useYn)) {
            throw new IllegalArgumentException("사용 여부 값이 올바르지 않습니다.");
        }
        systemRoleMapper.updateRoleStatus(roleCd, useYn);
    }

    /* ── 특정 권한을 보유한 사원 목록 ── */
    @Transactional(readOnly = true)
    public List<EmpRoleDto> findEmpsByRoleCd(String roleCd) {
        return systemRoleMapper.findEmpsByRoleCd(roleCd);
    }

    /* ── 특정 사원의 권한 목록 ── */
    @Transactional(readOnly = true)
    public List<EmpRoleDto> findRolesByEmpNo(String empNo) {
        return systemRoleMapper.findRolesByEmpNo(empNo);
    }

    /* ── 권한 할당 대상 사원 목록 (계정 있는 재직자) ── */
    @Transactional(readOnly = true)
    public List<EmpRoleDto> findAllUsableEmps() {
        return systemRoleMapper.findAllUsableEmps();
    }

    /* ── 권한 할당 저장 (Delete-Insert) ── */
    @Transactional
    public void assignRoles(RoleAssignDto dto) {
        if (isBlank(dto.getEmpNo())) {
            throw new IllegalArgumentException("사원번호는 필수 항목입니다.");
        }
        systemRoleMapper.deleteEmpRoles(dto.getEmpNo());
        if (dto.getRoleCdList() != null && !dto.getRoleCdList().isEmpty()) {
            String grntEmpNo = isBlank(dto.getGrntEmpNo()) ? dto.getEmpNo() : dto.getGrntEmpNo();
            for (String roleCd : dto.getRoleCdList()) {
                systemRoleMapper.insertEmpRole(dto.getEmpNo(), roleCd, grntEmpNo);
            }
        }
    }

    /* ── 유효성 검사 ── */
    private void validateRole(SystemRoleDto dto) {
        if (isBlank(dto.getRoleCd()))   throw new IllegalArgumentException("권한코드는 필수 입력 항목입니다.");
        if (isBlank(dto.getRoleNm()))   throw new IllegalArgumentException("권한명은 필수 입력 항목입니다.");
        if (dto.getRoleLevel() == null) throw new IllegalArgumentException("권한 레벨은 필수 입력 항목입니다.");
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
