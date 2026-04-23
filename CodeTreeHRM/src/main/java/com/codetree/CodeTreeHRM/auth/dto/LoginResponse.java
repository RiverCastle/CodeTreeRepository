package com.codetree.CodeTreeHRM.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String userId;
    private String empNo;
    private String empNm;
    private int roleLevel;
}
