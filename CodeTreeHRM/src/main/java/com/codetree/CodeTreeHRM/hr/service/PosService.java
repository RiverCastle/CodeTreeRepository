package com.codetree.CodeTreeHRM.hr.service;

import com.codetree.CodeTreeHRM.hr.dto.PosDto;
import com.codetree.CodeTreeHRM.hr.mapper.PosMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosService {

    private final PosMapper posMapper;

    /** 직급 전체 목록 */
    @Transactional(readOnly = true)
    public List<PosDto> findAll() {
        return posMapper.findAll();
    }

    /** 직급 단건 조회 */
    @Transactional(readOnly = true)
    public PosDto findByJbgdCd(String jbgdCd) {
        return posMapper.findByJbgdCd(jbgdCd);
    }

    /**
     * 직급 저장/수정 (Upsert)
     * - JBGD_CD 가 이미 존재하면 UPDATE, 없으면 INSERT
     */
    @Transactional
    public void savePos(PosDto pos) {
        validatePos(pos);
        if (posMapper.countByJbgdCd(pos.getJbgdCd()) > 0) {
            posMapper.updatePos(pos);
        } else {
            posMapper.insertPos(pos);
        }
    }

    /**
     * 직급 사용 여부 변경 (토글)
     * - '미사용' 처리 시 해당 직급을 사용 중인 재직 사원이 있으면 예외 발생
     */
    @Transactional
    public void updateStatus(String jbgdCd, String useYn) {
        if (posMapper.countByJbgdCd(jbgdCd) == 0) {
            throw new IllegalArgumentException("존재하지 않는 직급 코드입니다: " + jbgdCd);
        }
        if ("N".equals(useYn)) {
            int empCount = posMapper.countActiveEmployeesByJbgdCd(jbgdCd);
            if (empCount > 0) {
                throw new IllegalStateException(
                        "해당 직급을 사용 중인 재직 사원이 " + empCount + "명 있어 미사용 처리할 수 없습니다.");
            }
        }
        posMapper.updateStatus(jbgdCd, useYn);
    }

    /**
     * 다음 직급코드 자동 생성
     * POS-{nn} 패턴에서 최대 번호 + 1 을 2자리 0-패딩하여 반환
     * 예) POS-05 존재 시 → "POS-06"
     */
    @Transactional(readOnly = true)
    public String generateNextCode() {
        int nextSeq = posMapper.generateNextSeq();
        return String.format("POS-%02d", nextSeq);
    }

    // ── 유효성 검사 ────────────────────────────────────────────

    private void validatePos(PosDto pos) {
        if (pos.getJbgdCd() == null || pos.getJbgdCd().isBlank()) {
            throw new IllegalArgumentException("직급 코드는 필수입니다.");
        }
        if (pos.getJbgdNm() == null || pos.getJbgdNm().isBlank()) {
            throw new IllegalArgumentException("직급명은 필수입니다.");
        }
        if (pos.getJbgdOrdr() == null || pos.getJbgdOrdr() < 1) {
            throw new IllegalArgumentException("직급 순서는 1 이상의 숫자여야 합니다.");
        }
    }
}
