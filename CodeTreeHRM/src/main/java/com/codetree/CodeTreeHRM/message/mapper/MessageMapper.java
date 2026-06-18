package com.codetree.CodeTreeHRM.message.mapper;

import com.codetree.CodeTreeHRM.message.dto.MessageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    /** 캐시 적재용: 사용중(Y) 메시지 전체 */
    List<MessageDto> findAllForCache();

    /** 관리 화면 목록 (검색) */
    List<MessageDto> findAll(@Param("keyword") String keyword,
                             @Param("langCd") String langCd,
                             @Param("msgType") String msgType);

    /** 단건 조회 (복합키) */
    MessageDto findOne(@Param("msgId") String msgId,
                       @Param("langCd") String langCd);

    /** 존재 여부 (복합키) */
    int countOne(@Param("msgId") String msgId,
                 @Param("langCd") String langCd);

    void insert(MessageDto dto);

    void update(MessageDto dto);

    void updateStatus(@Param("msgId") String msgId,
                      @Param("langCd") String langCd,
                      @Param("isUse") String isUse);

    void delete(@Param("msgId") String msgId,
                @Param("langCd") String langCd);
}
