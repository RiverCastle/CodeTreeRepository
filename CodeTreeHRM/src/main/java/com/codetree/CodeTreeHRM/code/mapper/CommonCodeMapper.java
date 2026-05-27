package com.codetree.CodeTreeHRM.code.mapper;

import com.codetree.CodeTreeHRM.code.dto.CommonCodeDtlDto;
import com.codetree.CodeTreeHRM.code.dto.CommonCodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonCodeMapper {

    /* ── 그룹 코드 ── */
    List<CommonCodeDto> findAllGrps(@Param("grpNm") String grpNm,
                                    @Param("useYn") String useYn);

    CommonCodeDto findByGrpCd(@Param("grpCd") String grpCd);

    int countByGrpCd(@Param("grpCd") String grpCd);

    void insertGrp(CommonCodeDto dto);

    void updateGrp(CommonCodeDto dto);

    void updateGrpStatus(@Param("grpCd") String grpCd,
                         @Param("useYn") String useYn);

    /* ── 상세 코드 ── */
    List<CommonCodeDtlDto> findDtlsByGrpCd(@Param("grpCd") String grpCd);

    int countDtl(@Param("grpCd") String grpCd,
                 @Param("dtlCd") String dtlCd);

    void insertDtl(CommonCodeDtlDto dto);

    void updateDtl(CommonCodeDtlDto dto);

    void updateDtlStatus(@Param("grpCd") String grpCd,
                         @Param("dtlCd") String dtlCd,
                         @Param("useYn") String useYn);

    void deleteDtl(@Param("grpCd") String grpCd,
                   @Param("dtlCd") String dtlCd);
}
