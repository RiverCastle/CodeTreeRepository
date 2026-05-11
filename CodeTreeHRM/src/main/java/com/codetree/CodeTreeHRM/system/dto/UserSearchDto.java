package com.codetree.CodeTreeHRM.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchDto {

    private String userId;
    private String empNo;
    private String useYn;

    private int page = 1;
    private int size = 20;

    public int getOffset() {
        return (page - 1) * size;
    }
}
