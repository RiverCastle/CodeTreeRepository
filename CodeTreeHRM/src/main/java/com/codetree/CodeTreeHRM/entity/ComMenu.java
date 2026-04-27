package com.codetree.CodeTreeHRM.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "com_menu")
@Getter
@NoArgsConstructor
public class ComMenu {

    @Id
    @Column(name = "MENU_CD")
    private String menuCd;

    @Column(name = "MENU_NM", nullable = false)
    private String menuNm;

    @Column(name = "UPPER_MENU_CD")
    private String upperMenuCd;

    @Column(name = "MENU_URL")
    private String menuUrl;

    @Column(name = "MENU_ORDR", nullable = false)
    private Integer menuOrdr;

    @Column(name = "USE_YN", nullable = false)
    private String useYn;

    @Column(name = "REGIST_DT", insertable = false, updatable = false)
    private LocalDateTime registDt;

    @Column(name = "UPDT_DT", insertable = false, updatable = false)
    private LocalDateTime updtDt;
}
