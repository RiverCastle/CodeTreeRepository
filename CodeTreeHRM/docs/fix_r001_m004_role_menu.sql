-- =====================================================
-- 핫픽스: R001(ADMIN) - M004(시스템관리) 권한 복구
-- M004 및 모든 하위 메뉴를 R001에 일괄 재매핑
-- INSERT IGNORE: 이미 존재하는 행은 건너뜀
-- =====================================================

INSERT IGNORE INTO com_role_menu (ROLE_CD, MENU_CD, ACCS_YN, BASE_AUTH_YN)
SELECT 'R001', MENU_CD, 'Y', 'Y'
FROM com_menu
WHERE MENU_CD = 'M004'
   OR UPPER_MENU_CD = 'M004';

SELECT CONCAT('복구 완료: ', COUNT(*), '개 메뉴가 R001에 매핑되어 있습니다.') AS result
FROM com_role_menu
WHERE ROLE_CD = 'R001'
  AND MENU_CD IN (
      SELECT MENU_CD FROM com_menu WHERE MENU_CD = 'M004' OR UPPER_MENU_CD = 'M004'
  );
