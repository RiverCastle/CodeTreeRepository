package com.codetree.CodeTreeHRM.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleAssignDto {
    private String empNo;
    private List<String> roleCdList;
    private String grntEmpNo;   // 부여자 사번 (세션에서 추출 또는 프론트 전달)
}
