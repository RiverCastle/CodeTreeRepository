package com.codetree.CodeTreeHRM.message;

import com.codetree.CodeTreeHRM.common.exception.CustomException;
import com.codetree.CodeTreeHRM.message.dto.MessageDto;
import com.codetree.CodeTreeHRM.message.mapper.MessageMapper;
import com.codetree.CodeTreeHRM.message.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    private MessageDto msg(String id, String lang, String content) {
        MessageDto m = new MessageDto();
        m.setMsgId(id);
        m.setLangCd(lang);
        m.setMsgContent(content);
        m.setIsUse("Y");
        return m;
    }

    @BeforeEach
    void setUp() {
        when(messageMapper.findAllForCache()).thenReturn(List.of(
                msg("ERR_REQUIRED",  "ko", "{0}은(는) 필수 입력 항목입니다."),
                msg("ERR_DUPLICATE", "ko", "이미 등록된 {0}입니다. 다른 값을 입력해 주세요."),
                msg("ERR_REQUIRED",  "en", "{0} is a required field."),
                msg("INFO_SAVE",     "ko", "정상적으로 저장되었습니다.")
        ));
        messageService.reloadCache();
    }

    @Test
    @DisplayName("가변인자 치환 — {0} 파라미터가 결합된다")
    void getMessage_formats_args() {
        assertThat(messageService.getMessage("ERR_REQUIRED", "사원번호"))
                .isEqualTo("사원번호은(는) 필수 입력 항목입니다.");
        assertThat(messageService.getMessage("ERR_DUPLICATE", "20260604"))
                .isEqualTo("이미 등록된 20260604입니다. 다른 값을 입력해 주세요.");
    }

    @Test
    @DisplayName("파라미터가 없으면 패턴 원문을 반환한다")
    void getMessage_no_args_returns_pattern() {
        assertThat(messageService.getMessage("INFO_SAVE"))
                .isEqualTo("정상적으로 저장되었습니다.");
    }

    @Test
    @DisplayName("List 파라미터 오버로드도 동일하게 동작한다")
    void getMessage_list_overload() {
        assertThat(messageService.getMessage("ERR_DUPLICATE", List.of("EMP01")))
                .isEqualTo("이미 등록된 EMP01입니다. 다른 값을 입력해 주세요.");
    }

    @Test
    @DisplayName("언어 명시 — 해당 언어 패턴을 사용한다")
    void getMessage_with_language() {
        assertThat(messageService.getMessageByLang("en", "ERR_REQUIRED", "Name"))
                .isEqualTo("Name is a required field.");
    }

    @Test
    @DisplayName("미정의 언어는 기본 언어(ko)로 폴백한다")
    void getMessage_falls_back_to_default_lang() {
        assertThat(messageService.getMessageByLang("ja", "ERR_REQUIRED", "사번"))
                .isEqualTo("사번은(는) 필수 입력 항목입니다.");
    }

    @Test
    @DisplayName("미정의 코드는 코드 자체를 반환한다 (런타임 예외 방지)")
    void getMessage_unknown_code_returns_code() {
        assertThat(messageService.getMessage("NO_SUCH_CODE", "x")).isEqualTo("NO_SUCH_CODE");
    }

    @Test
    @DisplayName("CustomException 의 msgCd/args 가 그대로 문장으로 완성된다")
    void getMessage_resolves_custom_exception_payload() {
        CustomException ex = new CustomException("ERR_DUPLICATE", "20260604");
        assertThat(messageService.getMessage(ex.getMsgCd(), ex.getArgs()))
                .isEqualTo("이미 등록된 20260604입니다. 다른 값을 입력해 주세요.");
    }

    @Test
    @DisplayName("getCatalog — 언어별 msgId→content 맵을 반환한다")
    void getCatalog_returns_lang_map() {
        Map<String, String> ko = messageService.getCatalog("ko");
        assertThat(ko).containsKeys("ERR_REQUIRED", "ERR_DUPLICATE", "INFO_SAVE");
        assertThat(ko.get("ERR_REQUIRED")).isEqualTo("{0}은(는) 필수 입력 항목입니다.");
    }

    @Test
    @DisplayName("reloadCache — DB 를 다시 읽어 캐시를 재구성한다")
    void reloadCache_refreshes() {
        messageService.reloadCache();
        // setUp 1회 + 본문 1회 = 최소 2회 호출
        verify(messageMapper, atLeast(2)).findAllForCache();
    }
}
