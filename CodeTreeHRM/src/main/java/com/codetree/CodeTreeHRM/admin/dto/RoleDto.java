package com.codetree.CodeTreeHRM.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private String roleCd;
    private String roleNm;
    private Integer roleLevel;
    private String useYn;
}
