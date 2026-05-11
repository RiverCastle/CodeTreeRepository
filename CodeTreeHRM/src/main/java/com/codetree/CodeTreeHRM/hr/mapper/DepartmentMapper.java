package com.codetree.CodeTreeHRM.hr.mapper;

import com.codetree.CodeTreeHRM.hr.dto.DepartmentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    List<DepartmentDto> findAllDepts();

    DepartmentDto findByDeptCd(String deptCd);

    List<DepartmentDto> findDirectChildren(String upperDeptCd);

    int countByDeptCd(String deptCd);

    int countActiveChildren(String deptCd);

    int countActiveEmployees(String deptCd);

    void insertDepartment(DepartmentDto dept);

    void updateDepartment(DepartmentDto dept);

    void updateDeptLevel(@Param("deptCd") String deptCd, @Param("deptLvl") int deptLvl);

    void closeDepartment(String deptCd);

    /** DEPT-NNN 패턴에서 현재 최대 시퀀스 번호 조회 */
    int findMaxDeptSeq();
}
