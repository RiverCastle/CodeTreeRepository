package com.codetree.CodeTreeHRM.hr.service;

import com.codetree.CodeTreeHRM.hr.dto.DepartmentDto;
import com.codetree.CodeTreeHRM.hr.mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentMapper departmentMapper;

    /* ─────────────────────────────────────
       부서 트리 조회
       평탄 목록 → 계층 구조로 변환
    ───────────────────────────────────── */
    @Transactional(readOnly = true)
    public List<DepartmentDto> getDeptTree() {
        List<DepartmentDto> flatList = departmentMapper.findAllDepts();
        return buildTree(flatList);
    }

    private List<DepartmentDto> buildTree(List<DepartmentDto> flatList) {
        Map<String, DepartmentDto> map = new LinkedHashMap<>();
        for (DepartmentDto d : flatList) {
            d.setChildren(new ArrayList<>());
            map.put(d.getDeptCd(), d);
        }
        List<DepartmentDto> roots = new ArrayList<>();
        for (DepartmentDto d : flatList) {
            String parentCd = d.getUpperDeptCd();
            if (parentCd == null || !map.containsKey(parentCd)) {
                roots.add(d);
            } else {
                map.get(parentCd).getChildren().add(d);
            }
        }
        return roots;
    }

    /* ─────────────────────────────────────
       단건 상세 조회
    ───────────────────────────────────── */
    @Transactional(readOnly = true)
    public DepartmentDto findByDeptCd(String deptCd) {
        return departmentMapper.findByDeptCd(deptCd);
    }

    /* ─────────────────────────────────────
       저장 (Upsert: 신규 등록 / 수정)
    ───────────────────────────────────── */
    @Transactional
    public void saveDepartment(DepartmentDto dto) {
        validateRequired(dto);

        // 상위 부서 기준으로 레벨 자동 계산
        int newLevel = calcLevel(dto.getUpperDeptCd());
        dto.setDeptLvl(newLevel);

        boolean exists = departmentMapper.countByDeptCd(dto.getDeptCd()) > 0;
        if (exists) {
            DepartmentDto current = departmentMapper.findByDeptCd(dto.getDeptCd());
            departmentMapper.updateDepartment(dto);
            // 상위 부서 변경으로 레벨이 달라졌으면 하위 부서 레벨 cascade 갱신
            Integer prevLevel = current.getDeptLvl();
            if (prevLevel == null || prevLevel != newLevel) {
                cascadeUpdateLevel(dto.getDeptCd(), newLevel);
            }
        } else {
            departmentMapper.insertDepartment(dto);
        }
    }

    private int calcLevel(String upperDeptCd) {
        if (upperDeptCd == null || upperDeptCd.isBlank()) {
            return 1;
        }
        DepartmentDto parent = departmentMapper.findByDeptCd(upperDeptCd);
        if (parent == null) return 1;
        return (parent.getDeptLvl() != null ? parent.getDeptLvl() : 1) + 1;
    }

    /** 하위 부서 레벨 재귀 갱신 */
    private void cascadeUpdateLevel(String parentDeptCd, int parentLevel) {
        List<DepartmentDto> children = departmentMapper.findDirectChildren(parentDeptCd);
        for (DepartmentDto child : children) {
            int childLevel = parentLevel + 1;
            departmentMapper.updateDeptLevel(child.getDeptCd(), childLevel);
            cascadeUpdateLevel(child.getDeptCd(), childLevel);
        }
    }

    /* ─────────────────────────────────────
       폐지 처리 (USE_YN = 'N', CLSG_YMD = 오늘)
    ───────────────────────────────────── */
    @Transactional
    public void closeDepartment(String deptCd) {
        if (departmentMapper.countActiveChildren(deptCd) > 0) {
            throw new IllegalArgumentException(
                "하위 부서가 존재하여 폐지할 수 없습니다. 하위 부서를 먼저 처리해 주세요.");
        }
        if (departmentMapper.countActiveEmployees(deptCd) > 0) {
            throw new IllegalArgumentException(
                "재직 중인 사원이 있어 폐지할 수 없습니다. 사원 소속을 먼저 변경해 주세요.");
        }
        departmentMapper.closeDepartment(deptCd);
    }

    /* ─────────────────────────────────────
       유효성 검사
    ───────────────────────────────────── */
    private void validateRequired(DepartmentDto dto) {
        if (isBlank(dto.getDeptCd()))  throw new IllegalArgumentException("부서코드는 필수 입력 항목입니다.");
        if (isBlank(dto.getDeptNm()))  throw new IllegalArgumentException("부서명은 필수 입력 항목입니다.");
        if (dto.getDeptOrdr() == null) throw new IllegalArgumentException("출력 순서는 필수 입력 항목입니다.");
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
