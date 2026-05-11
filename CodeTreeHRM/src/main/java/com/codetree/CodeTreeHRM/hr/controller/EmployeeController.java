package com.codetree.CodeTreeHRM.hr.controller;

import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import com.codetree.CodeTreeHRM.hr.dto.EmployeeDto;
import com.codetree.CodeTreeHRM.hr.dto.EmployeeSearchDto;
import com.codetree.CodeTreeHRM.hr.dto.JobGradeDto;
import com.codetree.CodeTreeHRM.hr.dto.PageResponse;
import com.codetree.CodeTreeHRM.hr.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<ApiResponse<PageResponse<EmployeeDto>>> searchEmployees(EmployeeSearchDto search) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.searchEmployees(search)));
    }

    @GetMapping("/employees/{empNo}")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployee(@PathVariable String empNo) {
        EmployeeDto emp = employeeService.findByEmpNo(empNo);
        if (emp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(emp));
    }

    @PostMapping("/employees")
    public ResponseEntity<ApiResponse<String>> createEmployee(@RequestBody EmployeeDto dto) {
        try {
            String empNo = employeeService.createEmployee(dto);
            return ResponseEntity.ok(ApiResponse.success(empNo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/employees/{empNo}")
    public ResponseEntity<ApiResponse<Void>> updateEmployee(
            @PathVariable String empNo,
            @RequestBody EmployeeDto dto) {
        try {
            employeeService.updateEmployee(empNo, dto);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/job-grades")
    public ResponseEntity<ApiResponse<List<JobGradeDto>>> getJobGrades() {
        return ResponseEntity.ok(ApiResponse.success(employeeService.findAllJobGrades()));
    }
}
