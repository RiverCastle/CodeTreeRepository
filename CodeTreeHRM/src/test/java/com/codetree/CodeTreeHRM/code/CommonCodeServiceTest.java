package com.codetree.CodeTreeHRM.code;

import com.codetree.CodeTreeHRM.code.dto.CommonCodeDtlDto;
import com.codetree.CodeTreeHRM.code.dto.CommonCodeDto;
import com.codetree.CodeTreeHRM.code.mapper.CommonCodeMapper;
import com.codetree.CodeTreeHRM.code.service.CommonCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonCodeServiceTest {

    @Mock
    private CommonCodeMapper commonCodeMapper;

    @InjectMocks
    private CommonCodeService commonCodeService;

    private CommonCodeDto grpDto;
    private CommonCodeDtlDto dtlDto;

    @BeforeEach
    void setUp() {
        grpDto = new CommonCodeDto();
        grpDto.setGrpCd("TEST_GRP");
        grpDto.setGrpNm("테스트 그룹");
        grpDto.setUseYn("Y");

        dtlDto = new CommonCodeDtlDto();
        dtlDto.setDtlCd("TEST_DTL");
        dtlDto.setDtlNm("테스트 상세");
        dtlDto.setDtlOrdr(1);
        dtlDto.setUseYn("Y");
    }

    /* ── 그룹 코드 ── */

    @Test
    @DisplayName("신규 그룹 등록 — insertGrp 호출")
    void saveGrp_insert_when_new() {
        when(commonCodeMapper.countByGrpCd("TEST_GRP")).thenReturn(0);

        commonCodeService.saveGrp(grpDto);

        verify(commonCodeMapper, times(1)).insertGrp(grpDto);
        verify(commonCodeMapper, never()).updateGrp(any());
    }

    @Test
    @DisplayName("기존 그룹 수정 — updateGrp 호출")
    void saveGrp_update_when_exists() {
        when(commonCodeMapper.countByGrpCd("TEST_GRP")).thenReturn(1);

        commonCodeService.saveGrp(grpDto);

        verify(commonCodeMapper, times(1)).updateGrp(grpDto);
        verify(commonCodeMapper, never()).insertGrp(any());
    }

    @Test
    @DisplayName("그룹코드 없음 — 예외 발생")
    void saveGrp_no_grpCd_throws() {
        grpDto.setGrpCd("");

        assertThatThrownBy(() -> commonCodeService.saveGrp(grpDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹코드");

        verify(commonCodeMapper, never()).insertGrp(any());
    }

    @Test
    @DisplayName("그룹명 없음 — 예외 발생")
    void saveGrp_no_grpNm_throws() {
        grpDto.setGrpNm("  ");

        assertThatThrownBy(() -> commonCodeService.saveGrp(grpDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹명");

        verify(commonCodeMapper, never()).insertGrp(any());
    }

    @Test
    @DisplayName("그룹 목록 조회 — mapper 위임 및 결과 반환")
    void findAllGrps_delegates_to_mapper() {
        when(commonCodeMapper.findAllGrps(null, null)).thenReturn(List.of(grpDto));

        List<CommonCodeDto> result = commonCodeService.findAllGrps(null, null);

        assertThat(result).hasSize(1);
        verify(commonCodeMapper, times(1)).findAllGrps(null, null);
    }

    /* ── 상세 코드 ── */

    @Test
    @DisplayName("신규 상세 코드 등록 — insertDtl 호출")
    void saveDtl_insert_when_new() {
        when(commonCodeMapper.countDtl("TEST_GRP", "TEST_DTL")).thenReturn(0);

        commonCodeService.saveDtl("TEST_GRP", dtlDto);

        verify(commonCodeMapper, times(1)).insertDtl(dtlDto);
        verify(commonCodeMapper, never()).updateDtl(any());
    }

    @Test
    @DisplayName("기존 상세 코드 수정 — updateDtl 호출")
    void saveDtl_update_when_exists() {
        when(commonCodeMapper.countDtl("TEST_GRP", "TEST_DTL")).thenReturn(1);

        commonCodeService.saveDtl("TEST_GRP", dtlDto);

        verify(commonCodeMapper, times(1)).updateDtl(dtlDto);
        verify(commonCodeMapper, never()).insertDtl(any());
    }

    @Test
    @DisplayName("상세 코드 목록 조회 — mapper 위임 및 결과 반환")
    void findDtlsByGrpCd_delegates_to_mapper() {
        when(commonCodeMapper.findDtlsByGrpCd("TEST_GRP")).thenReturn(List.of(dtlDto));

        List<CommonCodeDtlDto> result = commonCodeService.findDtlsByGrpCd("TEST_GRP");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDtlCd()).isEqualTo("TEST_DTL");
        verify(commonCodeMapper, times(1)).findDtlsByGrpCd("TEST_GRP");
    }

    @Test
    @DisplayName("상세 코드 삭제 — mapper 위임")
    void deleteDtl_delegates_to_mapper() {
        commonCodeService.deleteDtl("TEST_GRP", "TEST_DTL");

        verify(commonCodeMapper, times(1)).deleteDtl("TEST_GRP", "TEST_DTL");
    }

    @Test
    @DisplayName("상세 코드 삭제 — grpCd 공백이면 예외")
    void deleteDtl_no_grpCd_throws() {
        assertThatThrownBy(() -> commonCodeService.deleteDtl("", "TEST_DTL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹코드");

        verify(commonCodeMapper, never()).deleteDtl(any(), any());
    }

    @Test
    @DisplayName("상세 코드 삭제 — dtlCd 공백이면 예외")
    void deleteDtl_no_dtlCd_throws() {
        assertThatThrownBy(() -> commonCodeService.deleteDtl("TEST_GRP", "  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상세코드");

        verify(commonCodeMapper, never()).deleteDtl(any(), any());
    }
}
