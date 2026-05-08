-- =====================================================
-- 핫픽스: M003-07 사이드바 미노출 문제 해결
-- com_menu에만 추가된 M003-07을 com_role_menu에 일괄 매핑
-- =====================================================

-- M003-07의 USE_YN 확인 및 보정 (NULL인 경우 대비)
UPDATE com_menu SET USE_YN = 'Y' WHERE MENU_CD = 'M003-07' AND (USE_YN IS NULL OR USE_YN = '');

-- 모든 활성 권한에 M003-07 접근 권한 부여
INSERT IGNORE INTO com_role_menu (ROLE_CD, MENU_CD, ACCS_YN, BASE_AUTH_YN)
SELECT ROLE_CD, 'M003-07', 'Y', 'Y'
FROM com_role
WHERE USE_YN = 'Y';

SELECT '완료: M003-07이 모든 활성 권한에 매핑되었습니다.' AS result;
