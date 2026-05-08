package com.codetree.CodeTreeHRM.hr.service;

import com.codetree.CodeTreeHRM.common.util.AesEncryptUtil;
import com.codetree.CodeTreeHRM.hr.dto.DeptDto;
import com.codetree.CodeTreeHRM.hr.dto.EmployeeDto;
import com.codetree.CodeTreeHRM.hr.dto.EmployeeSearchDto;
import com.codetree.CodeTreeHRM.hr.dto.JobGradeDto;
import com.codetree.CodeTreeHRM.hr.dto.PageResponse;
import com.codetree.CodeTreeHRM.hr.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final AesEncryptUtil aesEncryptUtil;

    @Transactional(readOnly = true)
    public PageResponse<EmployeeDto> searchEmployees(EmployeeSearchDto search) {
        List<EmployeeDto> list = employeeMapper.searchEmployees(search);
        long totalCount = employeeMapper.countEmployees(search);
        return PageResponse.of(list, totalCount, search.getPage(), search.getSize());
    }

    @Transactional(readOnly = true)
    public EmployeeDto findByEmpNo(String empNo) {
        EmployeeDto emp = employeeMapper.findByEmpNo(empNo);
        if (emp == null) return null;
        if (emp.getRsdntNo() != null) {
            emp.setRsdntNo(aesEncryptUtil.decrypt(emp.getRsdntNo()));
        }
        return emp;
    }

    @Transactional
    public String createEmployee(EmployeeDto dto) {
        validateRequired(dto);
        validateRetirementDate(dto);

        int year = LocalDate.now().getYear();
        int seq = employeeMapper.generateNextSeq(year);
        dto.setEmpNo(String.format("%d-%03d", year, seq));

        if (dto.getRsdntNo() != null && !dto.getRsdntNo().isBlank()) {
            dto.setRsdntNo(aesEncryptUtil.encrypt(dto.getRsdntNo()));
        }

        employeeMapper.insertEmployee(dto);
        return dto.getEmpNo();
    }

    @Transactional
    public void updateEmployee(String empNo, EmployeeDto dto) {
        validateRequired(dto);
        validateRetirementDate(dto);
        dto.setEmpNo(empNo);

        if (dto.getRsdntNo() != null && !dto.getRsdntNo().isBlank()) {
            dto.setRsdntNo(aesEncryptUtil.encrypt(dto.getRsdntNo()));
        } else {
            dto.setRsdntNo(null);
        }

        employeeMapper.updateEmployee(dto);
    }

    @Transactional(readOnly = true)
    public List<DeptDto> findAllDepts() {
        return employeeMapper.findAllDepts();
    }

    @Transactional(readOnly = true)
    public List<JobGradeDto> findAllJobGrades() {
        return employeeMapper.findAllJobGrades();
    }

    private void validateRequired(EmployeeDto dto) {
        if (isBlank(dto.getEmpNm()))     throw new IllegalArgumentException("성명은 필수 입력 항목입니다.");
        if (isBlank(dto.getDeptCd()))    throw new IllegalArgumentException("부서는 필수 입력 항목입니다.");
        if (isBlank(dto.getJbgdCd()))    throw new IllegalArgumentException("직급은 필수 입력 항목입니다.");
        if (isBlank(dto.getJncmpYmd()))  throw new IllegalArgumentException("입사일은 필수 입력 항목입니다.");
        if (isBlank(dto.getEmpSttsCd())) throw new IllegalArgumentException("재직상태는 필수 입력 항목입니다.");
        if (isBlank(dto.getEmplSeCd()))  throw new IllegalArgumentException("고용형태는 필수 입력 항목입니다.");
    }

    private void validateRetirementDate(EmployeeDto dto) {
        boolean isResigned = "RESIGNED".equals(dto.getEmpSttsCd());
        boolean hasRetirDate = !isBlank(dto.getRetirYmd());

        if (isResigned && !hasRetirDate) {
            throw new IllegalArgumentException("퇴직 처리 시 퇴직일은 필수 입력 항목입니다.");
        }
        if (!isResigned && hasRetirDate) {
            throw new IllegalArgumentException("퇴직일은 재직상태가 '퇴직'일 때만 입력 가능합니다.");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
