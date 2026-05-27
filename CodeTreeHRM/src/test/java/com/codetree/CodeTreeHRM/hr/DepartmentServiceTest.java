package com.codetree.CodeTreeHRM.hr;

import com.codetree.CodeTreeHRM.hr.dto.DepartmentDto;
import com.codetree.CodeTreeHRM.hr.mapper.DepartmentMapper;
import com.codetree.CodeTreeHRM.hr.service.DepartmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    @DisplayName("findAllDepts — mapper 위임 및 결과 반환")
    void findAllDepts_delegates_to_mapper() {
        DepartmentDto d1 = new DepartmentDto();
        d1.setDeptCd("DEPT-001");
        d1.setDeptNm("개발팀");

        DepartmentDto d2 = new DepartmentDto();
        d2.setDeptCd("DEPT-002");
        d2.setDeptNm("인사팀");

        when(departmentMapper.findAllDepts()).thenReturn(List.of(d1, d2));

        List<DepartmentDto> result = departmentService.findAllDepts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDeptCd()).isEqualTo("DEPT-001");
        verify(departmentMapper, times(1)).findAllDepts();
    }

    @Test
    @DisplayName("findAllDepts — 부서 없을 때 빈 리스트 반환")
    void findAllDepts_returns_empty_list_when_no_data() {
        when(departmentMapper.findAllDepts()).thenReturn(List.of());

        List<DepartmentDto> result = departmentService.findAllDepts();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getDeptTree — 부모-자식 트리 구조 변환")
    void getDeptTree_builds_hierarchy() {
        DepartmentDto root = new DepartmentDto();
        root.setDeptCd("DEPT-001");
        root.setDeptNm("본부");
        root.setUpperDeptCd(null);
        root.setDeptLvl(1);

        DepartmentDto child = new DepartmentDto();
        child.setDeptCd("DEPT-002");
        child.setDeptNm("개발팀");
        child.setUpperDeptCd("DEPT-001");
        child.setDeptLvl(2);

        when(departmentMapper.findAllDepts()).thenReturn(List.of(root, child));

        List<DepartmentDto> tree = departmentService.getDeptTree();

        assertThat(tree).hasSize(1);
        assertThat(tree.get(0).getDeptCd()).isEqualTo("DEPT-001");
        assertThat(tree.get(0).getChildren()).hasSize(1);
        assertThat(tree.get(0).getChildren().get(0).getDeptCd()).isEqualTo("DEPT-002");
    }

    @Test
    @DisplayName("saveDepartment — 부서코드 없으면 예외")
    void saveDepartment_no_code_throws() {
        DepartmentDto dto = new DepartmentDto();
        dto.setDeptNm("개발팀");
        dto.setDeptOrdr(1);

        assertThatThrownBy(() -> departmentService.saveDepartment(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("부서코드");
    }

    @Test
    @DisplayName("saveDepartment — 부서명 없으면 예외")
    void saveDepartment_no_name_throws() {
        DepartmentDto dto = new DepartmentDto();
        dto.setDeptCd("DEPT-001");
        dto.setDeptOrdr(1);

        assertThatThrownBy(() -> departmentService.saveDepartment(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("부서명");
    }

    @Test
    @DisplayName("saveDepartment — 신규 등록 (코드 없으면 insert 호출)")
    void saveDepartment_insert_when_new() {
        DepartmentDto dto = new DepartmentDto();
        dto.setDeptCd("DEPT-010");
        dto.setDeptNm("신규팀");
        dto.setDeptOrdr(10);

        when(departmentMapper.countByDeptCd("DEPT-010")).thenReturn(0);

        departmentService.saveDepartment(dto);

        verify(departmentMapper, times(1)).insertDepartment(dto);
        verify(departmentMapper, never()).updateDepartment(any());
    }

    @Test
    @DisplayName("saveDepartment — 기존 코드면 update 호출")
    void saveDepartment_update_when_exists() {
        DepartmentDto existing = new DepartmentDto();
        existing.setDeptCd("DEPT-001");
        existing.setDeptLvl(1);

        DepartmentDto dto = new DepartmentDto();
        dto.setDeptCd("DEPT-001");
        dto.setDeptNm("변경팀");
        dto.setDeptOrdr(1);

        when(departmentMapper.countByDeptCd("DEPT-001")).thenReturn(1);
        when(departmentMapper.findByDeptCd("DEPT-001")).thenReturn(existing);

        departmentService.saveDepartment(dto);

        verify(departmentMapper, times(1)).updateDepartment(dto);
        verify(departmentMapper, never()).insertDepartment(any());
    }
}
