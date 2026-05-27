package com.codetree.CodeTreeHRM.notice.controller;

import com.codetree.CodeTreeHRM.common.dto.ApiResponse;
import com.codetree.CodeTreeHRM.notice.dto.NoticeDto;
import com.codetree.CodeTreeHRM.notice.dto.NoticeSearchDto;
import com.codetree.CodeTreeHRM.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /* ── 목록 조회 (관리자 + 직원 공통) ── */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchNotices(NoticeSearchDto dto) {
        List<NoticeDto> list  = noticeService.searchNotices(dto);
        int             total = noticeService.countNotices(dto);
        int             size  = dto.getSize() < 1 ? 20 : dto.getSize();
        int             page  = dto.getPage() < 1 ? 1  : dto.getPage();

        Map<String, Object> data = new HashMap<>();
        data.put("content",    list);
        data.put("page",       page);
        data.put("totalPages", (int) Math.ceil((double) total / size));
        data.put("totalCount", total);

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /* ── 상세 조회 (조회수 증가) ── */
    @GetMapping("/{ntcNo}")
    public ResponseEntity<ApiResponse<NoticeDto>> getNotice(@PathVariable Long ntcNo) {
        NoticeDto dto = noticeService.getNoticeDetail(ntcNo);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    /* ── 등록 / 수정 ── */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveNotice(@RequestBody NoticeDto dto) {
        noticeService.saveNotice(dto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* ── 삭제 ── */
    @DeleteMapping("/{ntcNo}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable Long ntcNo) {
        noticeService.deleteNotice(ntcNo);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* ── 팝업 공지 목록 (게시 + 팝업 ON) ── */
    @GetMapping("/popups")
    public ResponseEntity<ApiResponse<List<NoticeDto>>> getPopups() {
        return ResponseEntity.ok(ApiResponse.success(noticeService.findActivePopups()));
    }
}
