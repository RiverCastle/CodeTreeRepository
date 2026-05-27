-- =====================================================
-- 공통 코드 관리 테이블 생성 및 샘플 데이터
-- 기존 테이블 수정 없음 — 신규 테이블 2개 생성
-- =====================================================

SET NAMES utf8mb4;

-- ── 그룹 코드 마스터 ──
CREATE TABLE IF NOT EXISTS com_code (
    GRP_CD    VARCHAR(20)  NOT NULL PRIMARY KEY,
    GRP_NM    VARCHAR(100) NOT NULL,
    DC        VARCHAR(500) NULL,
    USE_YN    CHAR(1)      NOT NULL DEFAULT 'Y',
    REGIST_DT DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDT_DT   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ── 상세 코드 ──
CREATE TABLE IF NOT EXISTS com_code_dtl (
    GRP_CD    VARCHAR(20)  NOT NULL,
    DTL_CD    VARCHAR(20)  NOT NULL,
    DTL_NM    VARCHAR(100) NOT NULL,
    DTL_ORDR  INT          NOT NULL DEFAULT 1,
    DC        VARCHAR(500) NULL,
    USE_YN    CHAR(1)      NOT NULL DEFAULT 'Y',
    REGIST_DT DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDT_DT   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (GRP_CD, DTL_CD),
    FOREIGN KEY (GRP_CD) REFERENCES com_code(GRP_CD)
);

-- ── 샘플 그룹 코드 (기존 데이터 보존, INSERT IGNORE) ──
INSERT IGNORE INTO com_code (GRP_CD, GRP_NM, DC) VALUES
('EMPL_SE_CD',  '고용형태',   '사원의 고용 유형 (정규직/계약직 등)'),
('EMP_STTS_CD', '사원상태',   '사원의 현재 재직 상태'),
('SEXDSTN_CD',  '성별',       '사원의 성별 구분'),
('POPUP_POS',   '팝업 위치',  '공지사항 팝업 표시 위치 (9종)'),
('POPUP_SIZE',  '팝업 크기',  '공지사항 팝업 크기 (SM/MD/LG)');

-- ── 샘플 상세 코드 (INSERT IGNORE) ──
INSERT IGNORE INTO com_code_dtl (GRP_CD, DTL_CD, DTL_NM, DTL_ORDR, DC) VALUES
-- 고용형태
('EMPL_SE_CD', 'FULL',     '정규직',   1, NULL),
('EMPL_SE_CD', 'PART',     '계약직',   2, NULL),
-- 사원상태
('EMP_STTS_CD', 'ACTIVE',   '재직',     1, NULL),
('EMP_STTS_CD', 'LOA',      '휴직',     2, NULL),
('EMP_STTS_CD', 'INACTIVE', '비활성',   3, NULL),
('EMP_STTS_CD', 'RESIGNED', '퇴직',     4, NULL),
-- 성별
('SEXDSTN_CD', 'M', '남', 1, NULL),
('SEXDSTN_CD', 'F', '여', 2, NULL),
-- 팝업 위치
('POPUP_POS', 'TL', '좌상단',   1, NULL),
('POPUP_POS', 'TC', '중앙상단', 2, NULL),
('POPUP_POS', 'TR', '우상단',   3, NULL),
('POPUP_POS', 'ML', '좌중앙',   4, NULL),
('POPUP_POS', 'CC', '정중앙',   5, NULL),
('POPUP_POS', 'MR', '우중앙',   6, NULL),
('POPUP_POS', 'BL', '좌하단',   7, NULL),
('POPUP_POS', 'BC', '중앙하단', 8, NULL),
('POPUP_POS', 'BR', '우하단',   9, NULL),
-- 팝업 크기
('POPUP_SIZE', 'SM', '소 (260px)', 1, NULL),
('POPUP_SIZE', 'MD', '중 (360px)', 2, NULL),
('POPUP_SIZE', 'LG', '대 (480px)', 3, NULL);

SELECT '완료: com_code, com_code_dtl 생성 및 샘플 데이터 삽입' AS status;
