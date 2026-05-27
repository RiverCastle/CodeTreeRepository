package com.codetree.CodeTreeHRM.notice.mapper;

import com.codetree.CodeTreeHRM.notice.dto.NoticeDto;
import com.codetree.CodeTreeHRM.notice.dto.NoticeSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<NoticeDto> searchNotices(NoticeSearchDto dto);
    int countNotices(NoticeSearchDto dto);

    NoticeDto findByNtcNo(Long ntcNo);

    void insertNotice(NoticeDto dto);
    void updateNotice(NoticeDto dto);
    void deleteNotice(Long ntcNo);

    void incrementInqryCnt(@Param("ntcNo") Long ntcNo);

    List<NoticeDto> findActivePopups();
}
