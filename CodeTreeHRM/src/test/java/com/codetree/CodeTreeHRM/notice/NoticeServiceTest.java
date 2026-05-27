package com.codetree.CodeTreeHRM.notice;

import com.codetree.CodeTreeHRM.notice.dto.NoticeDto;
import com.codetree.CodeTreeHRM.notice.mapper.NoticeMapper;
import com.codetree.CodeTreeHRM.notice.service.NoticeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeService noticeService;

    private NoticeDto baseDto;

    @BeforeEach
    void setUp() {
        baseDto = new NoticeDto();
        baseDto.setNtcTtl("테스트 공지");
        baseDto.setNtcCn("테스트 내용");
        baseDto.setImprtntYn("N");
        baseDto.setUseYn("Y");
        baseDto.setPopupYn("N");
        baseDto.setPopupPosCd(null);
    }

    @Test
    @DisplayName("팝업 OFF 등록 — popupPosCd null 허용")
    void saveNotice_popup_off() {
        noticeService.saveNotice(baseDto);
        verify(noticeMapper, times(1)).insertNotice(baseDto);
    }

    @Test
    @DisplayName("팝업 ON + 위치 지정 등록 — 정상 처리")
    void saveNotice_popup_on_with_position() {
        baseDto.setPopupYn("Y");
        baseDto.setPopupPosCd("TL");
        noticeService.saveNotice(baseDto);
        verify(noticeMapper, times(1)).insertNotice(baseDto);
    }

    @Test
    @DisplayName("팝업 ON + 위치 미지정 — 예외 발생")
    void saveNotice_popup_on_without_position_throws() {
        baseDto.setPopupYn("Y");
        baseDto.setPopupPosCd(null);
        assertThrows(IllegalArgumentException.class, () -> noticeService.saveNotice(baseDto));
        verify(noticeMapper, never()).insertNotice(any());
    }

    @Test
    @DisplayName("제목 없음 — 예외 발생")
    void saveNotice_no_title_throws() {
        baseDto.setNtcTtl("");
        assertThrows(IllegalArgumentException.class, () -> noticeService.saveNotice(baseDto));
        verify(noticeMapper, never()).insertNotice(any());
    }

    @Test
    @DisplayName("내용 없음 — 예외 발생")
    void saveNotice_no_content_throws() {
        baseDto.setNtcCn("  ");
        assertThrows(IllegalArgumentException.class, () -> noticeService.saveNotice(baseDto));
        verify(noticeMapper, never()).insertNotice(any());
    }

    @Test
    @DisplayName("ntcNo 존재 시 update 호출")
    void saveNotice_update_when_ntcNo_present() {
        baseDto.setNtcNo(1L);
        noticeService.saveNotice(baseDto);
        verify(noticeMapper, times(1)).updateNotice(baseDto);
        verify(noticeMapper, never()).insertNotice(any());
    }
}
