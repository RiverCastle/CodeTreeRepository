package com.codetree.CodeTreeHRM.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 공통 메뉴 엔티티 (com_menu 테이블 매핑)
 *
 * <p>시스템 전체에서 사용하는 메뉴 구조를 관리한다.
 * UPPER_MENU_CD 를 통한 자기 참조(Self-Reference) 방식으로 트리 구조를 표현한다.</p>
 *
 * <pre>
 * 테이블 구조 예시:
 *   M001  사원관리  (UPPER_MENU_CD = NULL  → 최상위)
 *   M001-01  인사발령  (UPPER_MENU_CD = 'M001' → M001의 하위)
 * </pre>
 */
@Entity
@Table(name = "com_menu")
@Getter
@NoArgsConstructor
public class ComMenu {

    /** 메뉴 코드 (PK) - 최상위: M001 형식, 하위: M001-01 형식 */
    @Id
    @Column(name = "MENU_CD")
    private String menuCd;

    /** 메뉴명 (필수) */
    @Column(name = "MENU_NM", nullable = false)
    private String menuNm;

    /** 메뉴 아이콘 (이모지 문자열, 예: 👤 🏢 ⚙️) */
    @Column(name = "MENU_ICON")
    private String menuIcon;

    /** 상위 메뉴 코드 - NULL이면 최상위 메뉴 */
    @Column(name = "UPPER_MENU_CD")
    private String upperMenuCd;

    /** 메뉴 URL - 클릭 시 이동할 경로 (예: /hr/appointment), 폴더형 메뉴는 NULL */
    @Column(name = "MENU_URL")
    private String menuUrl;

    /** 메뉴 정렬 순서 (같은 레벨 내에서 오름차순 정렬, 필수) */
    @Column(name = "MENU_ORDR", nullable = false)
    private Integer menuOrdr;

    /** 사용 여부 ('Y': 사용, 'N': 미사용) - 'N'이면 사이드바에 노출되지 않음 */
    @Column(name = "USE_YN", nullable = false)
    private String useYn;

    /** 등록 일시 - DB DEFAULT(CURRENT_TIMESTAMP)로 자동 설정, 애플리케이션에서 수정 불가 */
    @Column(name = "REGIST_DT", insertable = false, updatable = false)
    private LocalDateTime registDt;

    /** 수정 일시 - DB ON UPDATE CURRENT_TIMESTAMP로 자동 갱신, 애플리케이션에서 수정 불가 */
    @Column(name = "UPDT_DT", insertable = false, updatable = false)
    private LocalDateTime updtDt;

    /**
     * 신규 메뉴 생성 팩토리 메서드
     *
     * <p>JPA 엔티티는 기본 생성자가 필요하므로 직접 new ComMenu()를 사용하지 않고
     * 이 메서드를 통해 생성한다. 필드 직접 접근 없이 의미 있는 상태로 객체를 초기화한다.</p>
     *
     * @param menuCd      메뉴 코드 (PK)
     * @param menuNm      메뉴명
     * @param menuIcon    메뉴 아이콘 (이모지)
     * @param upperMenuCd 상위 메뉴 코드 (최상위면 null)
     * @param menuUrl     메뉴 URL (폴더형이면 null)
     * @param menuOrdr    정렬 순서
     * @param useYn       사용 여부 ('Y' / 'N')
     * @return 초기화된 ComMenu 인스턴스
     */
    public static ComMenu create(String menuCd, String menuNm, String menuIcon,
                                  String upperMenuCd, String menuUrl,
                                  Integer menuOrdr, String useYn) {
        ComMenu m = new ComMenu();
        m.menuCd      = menuCd;
        m.menuNm      = menuNm;
        m.menuIcon    = menuIcon;
        m.upperMenuCd = upperMenuCd;
        m.menuUrl     = menuUrl;
        m.menuOrdr    = menuOrdr;
        m.useYn       = useYn;
        return m;
    }

    /**
     * 메뉴 정보 수정 메서드
     *
     * <p>메뉴코드(PK)는 변경 불가하며, 나머지 업무 필드만 갱신한다.
     * REGIST_DT / UPDT_DT 는 DB 트리거로 자동 처리된다.</p>
     *
     * @param menuNm      변경할 메뉴명
     * @param menuIcon    변경할 아이콘
     * @param upperMenuCd 변경할 상위 메뉴 코드
     * @param menuUrl     변경할 메뉴 URL
     * @param menuOrdr    변경할 정렬 순서
     * @param useYn       변경할 사용 여부
     */
    public void update(String menuNm, String menuIcon, String upperMenuCd,
                       String menuUrl, Integer menuOrdr, String useYn) {
        this.menuNm      = menuNm;
        this.menuIcon    = menuIcon;
        this.upperMenuCd = upperMenuCd;
        this.menuUrl     = menuUrl;
        this.menuOrdr    = menuOrdr;
        this.useYn       = useYn;
    }
}
