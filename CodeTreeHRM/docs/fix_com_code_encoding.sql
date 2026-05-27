-- =====================================================
-- com_code GRP_NM, DC 인코딩 깨짐 데이터 보정
-- =====================================================

SET NAMES utf8mb4;

UPDATE com_code SET GRP_NM = '고용형태', DC = '사원의 고용 유형 (정규직/계약직 등)' WHERE GRP_CD = 'EMPL_SE_CD';
UPDATE com_code SET GRP_NM = '사원상태', DC = '사원의 현재 재직 상태'              WHERE GRP_CD = 'EMP_STTS_CD';
UPDATE com_code SET GRP_NM = '성별',     DC = '사원의 성별 구분'                   WHERE GRP_CD = 'SEXDSTN_CD';
UPDATE com_code SET GRP_NM = '팝업 위치', DC = '공지사항 팝업 표시 위치 (9종)'      WHERE GRP_CD = 'POPUP_POS';
UPDATE com_code SET GRP_NM = '팝업 크기', DC = '공지사항 팝업 크기 (SM/MD/LG)'      WHERE GRP_CD = 'POPUP_SIZE';

SELECT GRP_CD, GRP_NM, DC FROM com_code;
