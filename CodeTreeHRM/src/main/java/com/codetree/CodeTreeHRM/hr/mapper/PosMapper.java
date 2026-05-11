package com.codetree.CodeTreeHRM.hr.mapper;

import com.codetree.CodeTreeHRM.hr.dto.PosDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PosMapper {

    /** 직급 전체 목록 (JBGD_ORDR DESC) */
    List<PosDto> findAll();

    /** 특정 직급 상세 조회 */
    PosDto findByJbgdCd(@Param("jbgdCd") String jbgdCd);

    /** 직급코드 중복 체크 */
    int countByJbgdCd(@Param("jbgdCd") String jbgdCd);

    /** 해당 직급을 사용 중인 재직 사원 수 (비활성화 유효성 검사용) */
    int countActiveEmployeesByJbgdCd(@Param("jbgdCd") String jbgdCd);

    /** 신규 등록 */
    void insertPos(PosDto pos);

    /** 정보 수정 (UPDT_DT 자동 갱신) */
    void updatePos(PosDto pos);

    /** 사용 여부 변경 (UPDT_DT 자동 갱신) */
    void updateStatus(@Param("jbgdCd") String jbgdCd, @Param("useYn") String useYn);
}
