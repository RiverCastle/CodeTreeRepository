package com.codetree.CodeTreeHRM.repository;

import com.codetree.CodeTreeHRM.entity.ComMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 공통 메뉴 Repository
 *
 * <p>Spring Data JPA 인터페이스를 상속받아 com_menu 테이블에 대한 CRUD 및
 * 커스텀 쿼리 메서드를 제공한다.
 * 메서드명 기반 쿼리 자동 생성(Query Derivation) 방식을 사용한다.</p>
 */
public interface ComMenuRepository extends JpaRepository<ComMenu, String> {

    /**
     * 사이드바용 메뉴 조회 - 사용 여부가 'Y'인 메뉴만 정렬 순서 오름차순으로 반환
     *
     * <p>사이드바(menu_frame.html)에서 /api/menus 호출 시 사용.
     * 미사용(USE_YN='N') 메뉴는 제외하여 화면에 노출하지 않는다.</p>
     *
     * @param useYn 사용 여부 ('Y' 전달)
     * @return 사용 중인 메뉴 목록 (MENU_ORDR 오름차순)
     */
    List<ComMenu> findByUseYnOrderByMenuOrdr(String useYn);

    /**
     * 관리화면용 전체 메뉴 조회 - USE_YN 구분 없이 모든 메뉴를 정렬 순서 오름차순으로 반환
     *
     * <p>메뉴관리 화면(menu-mng.html)에서 /api/menus/all 호출 시 사용.
     * 미사용 메뉴도 포함하여 관리자가 모든 메뉴를 확인·수정할 수 있도록 한다.</p>
     *
     * @return 전체 메뉴 목록 (MENU_ORDR 오름차순)
     */
    List<ComMenu> findAllByOrderByMenuOrdr();

    /**
     * 최상위 메뉴 목록 조회 - UPPER_MENU_CD 가 NULL인 메뉴만 반환
     *
     * <p>신규 메뉴 코드 자동 채번 시 최상위 레벨의 최대 번호를 구하는 데 사용.
     * 예: M001, M002, M003 존재 시 → 다음 코드 = M004</p>
     *
     * @return 최상위 메뉴 목록
     */
    List<ComMenu> findByUpperMenuCdIsNull();

    /**
     * 특정 부모 메뉴의 자식 메뉴 목록 조회
     *
     * <p>신규 하위 메뉴 코드 자동 채번 시 해당 부모의 자식 중 최대 suffix를 구하는 데 사용.
     * 예: M001 하위에 M001-01, M001-02 존재 시 → 다음 코드 = M001-03</p>
     *
     * @param upperMenuCd 상위 메뉴 코드
     * @return 해당 부모의 자식 메뉴 목록
     */
    List<ComMenu> findByUpperMenuCd(String upperMenuCd);
}
