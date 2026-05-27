package com.codetree.CodeTreeHRM.code.service;

import com.codetree.CodeTreeHRM.code.dto.CommonCodeDtlDto;
import com.codetree.CodeTreeHRM.code.dto.CommonCodeDto;
import com.codetree.CodeTreeHRM.code.mapper.CommonCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeService {

    private final CommonCodeMapper commonCodeMapper;

    /* ─────────────────────────────────────
       그룹 코드 목록 조회
    ───────────────────────────────────── */
    @Transactional(readOnly = true)
    public List<CommonCodeDto> findAllGrps(String grpNm, String useYn) {
        return commonCodeMapper.findAllGrps(
                isBlank(grpNm) ? null : grpNm,
                isBlank(useYn) ? null : useYn);
    }

    /* ─────────────────────────────────────
       그룹 코드 저장 (Upsert)
    ───────────────────────────────────── */
    @Transactional
    public void saveGrp(CommonCodeDto dto) {
        if (isBlank(dto.getGrpCd())) throw new IllegalArgumentException("그룹코드는 필수 입력 항목입니다.");
        if (isBlank(dto.getGrpNm())) throw new IllegalArgumentException("그룹명은 필수 입력 항목입니다.");

        if (commonCodeMapper.countByGrpCd(dto.getGrpCd()) > 0) {
            commonCodeMapper.updateGrp(dto);
        } else {
            commonCodeMapper.insertGrp(dto);
        }
    }

    /* ─────────────────────────────────────
       그룹 사용여부 변경
    ───────────────────────────────────── */
    @Transactional
    public void updateGrpStatus(String grpCd, String useYn) {
        commonCodeMapper.updateGrpStatus(grpCd, useYn);
    }

    /* ─────────────────────────────────────
       상세 코드 목록 조회
    ───────────────────────────────────── */
    @Transactional(readOnly = true)
    public List<CommonCodeDtlDto> findDtlsByGrpCd(String grpCd) {
        return commonCodeMapper.findDtlsByGrpCd(grpCd);
    }

    /* ─────────────────────────────────────
       상세 코드 저장 (Upsert)
    ───────────────────────────────────── */
    @Transactional
    public void saveDtl(String grpCd, CommonCodeDtlDto dto) {
        if (isBlank(grpCd))         throw new IllegalArgumentException("그룹코드는 필수입니다.");
        if (isBlank(dto.getDtlCd())) throw new IllegalArgumentException("상세코드는 필수 입력 항목입니다.");
        if (isBlank(dto.getDtlNm())) throw new IllegalArgumentException("상세명은 필수 입력 항목입니다.");

        dto.setGrpCd(grpCd);

        if (commonCodeMapper.countDtl(grpCd, dto.getDtlCd()) > 0) {
            commonCodeMapper.updateDtl(dto);
        } else {
            commonCodeMapper.insertDtl(dto);
        }
    }

    /* ─────────────────────────────────────
       상세 코드 사용여부 변경
    ───────────────────────────────────── */
    @Transactional
    public void updateDtlStatus(String grpCd, String dtlCd, String useYn) {
        commonCodeMapper.updateDtlStatus(grpCd, dtlCd, useYn);
    }

    /* ─────────────────────────────────────
       상세 코드 삭제
    ───────────────────────────────────── */
    @Transactional
    public void deleteDtl(String grpCd, String dtlCd) {
        if (isBlank(grpCd)) throw new IllegalArgumentException("그룹코드는 필수입니다.");
        if (isBlank(dtlCd)) throw new IllegalArgumentException("상세코드는 필수입니다.");
        commonCodeMapper.deleteDtl(grpCd, dtlCd);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
