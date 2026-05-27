package com.codetree.CodeTreeHRM.notice.service;

import com.codetree.CodeTreeHRM.notice.dto.NoticeDto;
import com.codetree.CodeTreeHRM.notice.dto.NoticeSearchDto;
import com.codetree.CodeTreeHRM.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;

    @Transactional(readOnly = true)
    public List<NoticeDto> searchNotices(NoticeSearchDto dto) {
        return noticeMapper.searchNotices(dto);
    }

    @Transactional(readOnly = true)
    public int countNotices(NoticeSearchDto dto) {
        return noticeMapper.countNotices(dto);
    }

    @Transactional(readOnly = true)
    public NoticeDto findByNtcNo(Long ntcNo) {
        return noticeMapper.findByNtcNo(ntcNo);
    }

    @Transactional
    public NoticeDto getNoticeDetail(Long ntcNo) {
        noticeMapper.incrementInqryCnt(ntcNo);
        return noticeMapper.findByNtcNo(ntcNo);
    }

    @Transactional
    public void saveNotice(NoticeDto dto) {
        validate(dto);
        if (dto.getNtcNo() == null) {
            if (dto.getImprtntYn() == null) dto.setImprtntYn("N");
            if (dto.getUseYn()    == null) dto.setUseYn("Y");
            if (dto.getPopupYn()  == null) dto.setPopupYn("N");
            noticeMapper.insertNotice(dto);
        } else {
            noticeMapper.updateNotice(dto);
        }
    }

    @Transactional
    public void deleteNotice(Long ntcNo) {
        noticeMapper.deleteNotice(ntcNo);
    }

    @Transactional(readOnly = true)
    public List<NoticeDto> findActivePopups() {
        return noticeMapper.findActivePopups();
    }

    private void validate(NoticeDto dto) {
        if (dto.getNtcTtl() == null || dto.getNtcTtl().isBlank())
            throw new IllegalArgumentException("제목은 필수 입력 항목입니다.");
        if (dto.getNtcCn() == null || dto.getNtcCn().isBlank())
            throw new IllegalArgumentException("내용은 필수 입력 항목입니다.");
        if ("Y".equals(dto.getPopupYn()) && (dto.getPopupPosCd() == null || dto.getPopupPosCd().isBlank()))
            throw new IllegalArgumentException("팝업 위치를 선택하세요.");
    }
}
