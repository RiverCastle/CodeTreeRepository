package com.codetree.CodeTreeHRM.notice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeSearchDto {
    private String ntcTtl;
    private String imprtntYn;
    private String useYn;
    private String fromDt;
    private String toDt;
    private int    page;
    private int    size;

    public int getOffset() {
        return (page < 1 ? 0 : page - 1) * (size < 1 ? 20 : size);
    }

    public int getLimit() {
        return size < 1 ? 20 : size;
    }
}
