-- =====================================================
-- Migration: com_menu 감사 컬럼 추가
-- 메뉴 등록일시(CRTN_DT), 수정일시(UPDT_DT) 컬럼 추가
-- =====================================================

ALTER TABLE com_menu
    ADD COLUMN CRTN_DT DATETIME NULL     COMMENT '등록일시',
    ADD COLUMN UPDT_DT DATETIME NULL     COMMENT '수정일시';

-- 기존 데이터에 등록일시 기본값 설정
UPDATE com_menu SET CRTN_DT = NOW() WHERE CRTN_DT IS NULL;
