package com.codetree.CodeTreeHRM.notice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDto {
    private Long    ntcNo;
    private String  ntcTtl;
    private String  ntcCn;
    private String  imprtntYn;
    private String  useYn;
    private String  popupYn;
    private String  popupPosCd;
    private int     inqryCnt;
    private String  registDt;
    private String  updtDt;
    private String  rgstrNo;
}
