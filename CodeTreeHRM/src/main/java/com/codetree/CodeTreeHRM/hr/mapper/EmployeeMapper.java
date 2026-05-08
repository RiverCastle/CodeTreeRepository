package com.codetree.CodeTreeHRM.hr.mapper;

import com.codetree.CodeTreeHRM.hr.dto.DeptDto;
import com.codetree.CodeTreeHRM.hr.dto.EmployeeDto;
import com.codetree.CodeTreeHRM.hr.dto.EmployeeSearchDto;
import com.codetree.CodeTreeHRM.hr.dto.JobGradeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<EmployeeDto> searchEmployees(EmployeeSearchDto search);

    long countEmployees(EmployeeSearchDto search);

    EmployeeDto findByEmpNo(String empNo);

    int generateNextSeq(@Param("year") int year);

    void insertEmployee(EmployeeDto employee);

    void updateEmployee(EmployeeDto employee);

    List<DeptDto> findAllDepts();

    List<JobGradeDto> findAllJobGrades();
}
