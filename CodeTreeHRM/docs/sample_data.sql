-- =====================================================
-- CodeTree HRM 샘플 데이터
-- =====================================================

SET NAMES utf8mb4;

-- ── 기존 데이터 정리 (의존성 역순) ──
DELETE FROM com_role_menu;
DELETE FROM com_emp_role WHERE EMP_NO != '2024-001';
DELETE FROM com_user WHERE EMP_NO != '2024-001';
DELETE FROM com_menu;
DELETE FROM hrm_emp_master WHERE EMP_NO != '2024-001';
DELETE FROM hrm_dept_master WHERE DEPT_CD != 'DEPT-001';
DELETE FROM hrm_pos_master WHERE JBGD_CD != 'POS-01';
DELETE FROM com_role WHERE ROLE_CD != 'R001';

-- ── 직급 ──
INSERT INTO hrm_pos_master (JBGD_CD, JBGD_NM, JBGD_ORDR) VALUES
('POS-02', '부장',   2),
('POS-03', '과장',   3),
('POS-04', '대리',   4),
('POS-05', '사원',   5);

-- ── 부서 ──
INSERT INTO hrm_dept_master (DEPT_CD, DEPT_NM, DEPT_ORDR) VALUES
('DEPT-002', '인사부',   2),
('DEPT-003', '개발부',   3),
('DEPT-004', '영업부',   4);

-- ── 사원 ──
INSERT INTO hrm_emp_master (EMP_NO, EMP_NM, DEPT_CD, JBGD_CD, JNCMP_YMD, EMP_STTS_CD, EMPL_SE_CD) VALUES
('2024-002', '김인사', 'DEPT-002', 'POS-02', '2020-03-01', 'ACTIVE', 'FULL'),
('2024-003', '이개발', 'DEPT-003', 'POS-03', '2021-06-01', 'ACTIVE', 'FULL'),
('2024-004', '박영업', 'DEPT-004', 'POS-04', '2022-01-15', 'ACTIVE', 'FULL'),
('2024-005', '최사원', 'DEPT-003', 'POS-05', '2023-09-01', 'ACTIVE', 'FULL');

-- ── 권한 ──
-- ROLE_LEVEL: 숫자가 낮을수록 높은 권한
INSERT INTO com_role (ROLE_CD, ROLE_NM, ROLE_LEVEL, DC) VALUES
('R002', 'HR_MANAGER',   2, '인사 관리자 - 사원/부서 관리 가능'),
('R003', 'DEPT_MANAGER', 3, '부서장 - 공통 관리 가능'),
('R004', 'EMPLOYEE',     5, '일반 사원 - 기본 메뉴 접근');

-- ── 사용자 계정 (비밀번호: 123456789) ──
INSERT INTO com_user (EMP_NO, USER_ID, USER_PWD, RGSTR_NO) VALUES
('2024-002', 'kimhr',     '$2a$10$kjGNr/urjKkKieFjzdI7e.7YOLhrdo56eiYAOFMbo2XwVHA94tGQW', '2024-001'),
('2024-003', 'leedev',    '$2a$10$kjGNr/urjKkKieFjzdI7e.7YOLhrdo56eiYAOFMbo2XwVHA94tGQW', '2024-001'),
('2024-004', 'parksale',  '$2a$10$kjGNr/urjKkKieFjzdI7e.7YOLhrdo56eiYAOFMbo2XwVHA94tGQW', '2024-001'),
('2024-005', 'choiemp',   '$2a$10$kjGNr/urjKkKieFjzdI7e.7YOLhrdo56eiYAOFMbo2XwVHA94tGQW', '2024-001');

-- ── 사원-권한 매핑 ──
INSERT INTO com_emp_role (EMP_NO, ROLE_CD, GRNT_EMP_NO) VALUES
('2024-002', 'R002', '2024-001'),
('2024-003', 'R003', '2024-001'),
('2024-004', 'R004', '2024-001'),
('2024-005', 'R004', '2024-001');

-- ── 메뉴 (부모) ──
INSERT INTO com_menu (MENU_CD, MENU_NM, UPPER_MENU_CD, MENU_URL, MENU_ORDR) VALUES
('M001', '사원관리',   NULL, NULL, 10),
('M002', '부서관리',   NULL, NULL, 20),
('M003', '공통관리',   NULL, NULL, 30),
('M004', '시스템관리', NULL, NULL, 40);

-- ── 메뉴 (자식) ──
INSERT INTO com_menu (MENU_CD, MENU_NM, UPPER_MENU_CD, MENU_URL, MENU_ORDR) VALUES
('M001-01', '인사발령',       'M001', '/hr/appointment',         11),
('M001-02', '인사정보',       'M001', '/hr/info',                12),
('M001-03', '사원등록',       'M001', '/hr/register',            13),
('M002-01', '부서 목록',      'M002', '/dept/list',              21),
('M002-02', '부서 등록',      'M002', '/dept/register',          22),
('M003-01', '공통코드 목록',  'M003', '/common/code/list',       31),
('M003-02', '공통코드 등록',  'M003', '/common/code/register',   32),
('M004-01', '사용자 관리',    'M004', '/system/user',            41),
('M004-02', '권한 관리',      'M004', '/system/role',            42),
('M004-03', '로그인 이력',    'M004', '/system/login-history',   43);

-- ── 권한-메뉴 매핑 (BASE_AUTH_YN 상속 모델) ──
--
--  BASE_AUTH_YN='Y' + ROLE_LEVEL N → ROLE_LEVEL <= N 인 사용자 모두 접근 가능
--
--  접근 범위 설계:
--    시스템관리(M004)  → R001 ADMIN(1)       : ADMIN 전용
--    사원/부서관리      → R002 HR_MANAGER(2)  : ADMIN + HR_MANAGER
--    공통관리(M003)    → R003 DEPT_MANAGER(3): ADMIN + HR_MANAGER + DEPT_MANAGER
--

INSERT INTO com_role_menu (ROLE_CD, MENU_CD, ACCS_YN, BASE_AUTH_YN) VALUES
-- 시스템관리 (ADMIN 전용, LEVEL 1)
('R001', 'M004',    'Y', 'Y'),
('R001', 'M004-01', 'Y', 'Y'),
('R001', 'M004-02', 'Y', 'Y'),
('R001', 'M004-03', 'Y', 'Y'),
-- 사원관리 (HR_MANAGER 이상, LEVEL 2)
('R002', 'M001',    'Y', 'Y'),
('R002', 'M001-01', 'Y', 'Y'),
('R002', 'M001-02', 'Y', 'Y'),
('R002', 'M001-03', 'Y', 'Y'),
-- 부서관리 (HR_MANAGER 이상, LEVEL 2)
('R002', 'M002',    'Y', 'Y'),
('R002', 'M002-01', 'Y', 'Y'),
('R002', 'M002-02', 'Y', 'Y'),
-- 공통관리 (DEPT_MANAGER 이상, LEVEL 3)
('R003', 'M003',    'Y', 'Y'),
('R003', 'M003-01', 'Y', 'Y'),
('R003', 'M003-02', 'Y', 'Y');

SELECT '완료' AS status;
