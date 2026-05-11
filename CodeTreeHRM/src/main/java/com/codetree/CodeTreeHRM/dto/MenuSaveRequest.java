package com.codetree.CodeTreeHRM.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 메뉴 등록/수정 요청 DTO (Data Transfer Object)
 *
 * <p>클라이언트(menu-mng.html)에서 전송하는 JSON 요청 바디를 받아오는 객체.
 * POST /api/menus (등록), PUT /api/menus/{menuCd} (수정) 요청에서 공통으로 사용한다.</p>
 *
 * <pre>
 * 요청 JSON 예시:
 * {
 *   "menuCd"     : "M001-04",
 *   "menuNm"     : "급여관리",
 *   "menuIcon"   : "💰",
 *   "upperMenuCd": "M001",
 *   "menuUrl"    : "/hr/salary",
 *   "menuOrdr"   : 4,
 *   "useYn"      : "Y"
 * }
 * </pre>
 */
@Getter
@Setter
public class MenuSaveRequest {

    /** 메뉴 코드 (등록 시 필수, 수정 시 URL PathVariable과 동일해야 함) */
    private String menuCd;

    /** 메뉴명 (필수) */
    private String menuNm;

    /** 메뉴 아이콘 - 이모지 문자열, 없으면 null */
    private String menuIcon;

    /** 상위 메뉴 코드 - 최상위 메뉴이면 null 또는 빈 문자열 */
    private String upperMenuCd;

    /** 메뉴 URL - 폴더형(하위 메뉴를 가진) 메뉴이면 null */
    private String menuUrl;

    /** 정렬 순서 - 같은 레벨 내 오름차순 */
    private Integer menuOrdr;

    /** 사용 여부 - 'Y': 사이드바 노출, 'N': 숨김 */
    private String useYn;
}
