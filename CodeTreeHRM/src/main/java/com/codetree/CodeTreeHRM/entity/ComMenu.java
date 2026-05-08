package com.codetree.CodeTreeHRM.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "com_menu")
@Getter
@Setter
@NoArgsConstructor
public class ComMenu {

    @Id
    @Column(name = "MENU_CD", length = 20)
    private String menuCd;

    @Column(name = "MENU_NM", nullable = false, length = 100)
    private String menuNm;

    @Column(name = "UPPER_MENU_CD", length = 20)
    private String upperMenuCd;

    @Column(name = "MENU_URL", length = 200)
    private String menuUrl;

    @Column(name = "MENU_ORDR", nullable = false)
    private int menuOrdr;

    @Column(name = "USE_YN", nullable = false, length = 1)
    private String useYn = "Y";

    @Column(name = "REGIST_DT", nullable = false, updatable = false)
    private LocalDateTime registDt;

    @Column(name = "UPDT_DT", nullable = false)
    private LocalDateTime updtDt;

    @PrePersist
    public void prePersist() {
        this.registDt = LocalDateTime.now();
        this.updtDt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updtDt = LocalDateTime.now();
    }
}