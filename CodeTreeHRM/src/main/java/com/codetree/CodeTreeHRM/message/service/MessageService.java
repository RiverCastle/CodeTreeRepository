package com.codetree.CodeTreeHRM.message.service;

import com.codetree.CodeTreeHRM.common.exception.CustomException;
import com.codetree.CodeTreeHRM.message.dto.MessageDto;
import com.codetree.CodeTreeHRM.message.mapper.MessageMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 공통 메시지 리졸버 + 인메모리 캐시.
 *
 * <p>시스템 기동 시 com_message(IS_USE='Y') 데이터를 메모리에 적재하고,
 * 런타임에는 DB 조회 없이 메시지 코드 + 파라미터로 문장을 완성한다.
 * 캐시 구조: {@code Map<langCd, Map<msgId, content>>}.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    /** 언어 미지정 시 기본 언어 */
    public static final String DEFAULT_LANG = "ko";

    private final MessageMapper messageMapper;

    /** langCd → (msgId → content) 2단계 인메모리 캐시 */
    private final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    /* ─────────────────────────────────────
       기동 시 캐시 적재
    ───────────────────────────────────── */
    @PostConstruct
    public void init() {
        reloadCache();
    }

    /* ─────────────────────────────────────
       캐시 새로고침 (관리자 대시보드 / 메시지 저장 후 호출)
    ───────────────────────────────────── */
    public void reloadCache() {
        Map<String, Map<String, String>> fresh = new ConcurrentHashMap<>();
        List<MessageDto> all = messageMapper.findAllForCache();
        for (MessageDto m : all) {
            fresh.computeIfAbsent(m.getLangCd(), k -> new ConcurrentHashMap<>())
                 .put(m.getMsgId(), m.getMsgContent());
        }
        // 원자적 교체
        cache.clear();
        cache.putAll(fresh);
        log.info("[MessageService] 메시지 캐시 적재 완료 - {} languages / {} entries",
                cache.size(), all.size());
    }

    /* ─────────────────────────────────────
       메시지 리졸버 (가변인자) - 기본 언어(ko)
    ───────────────────────────────────── */
    public String getMessage(String msgCd, Object... args) {
        return getMessageByLang(DEFAULT_LANG, msgCd, args);
    }

    /* ─────────────────────────────────────
       메시지 리졸버 (List 파라미터) - 기본 언어(ko)
    ───────────────────────────────────── */
    public String getMessage(String msgCd, List<Object> args) {
        return getMessageByLang(DEFAULT_LANG, msgCd, args == null ? new Object[0] : args.toArray());
    }

    /* ─────────────────────────────────────
       메시지 리졸버 (언어 명시 + 가변인자)
       ※ getMessage(code, arg) 와의 오버로드 모호성을 피하기 위해 별도 이름 사용
    ───────────────────────────────────── */
    public String getMessageByLang(String langCd, String msgCd, Object... args) {
        if (msgCd == null) return "";

        String pattern = lookup(langCd, msgCd);
        if (pattern == null) {
            // 미정의 코드 → 코드 자체를 반환하여 누락을 노출 (런타임 예외 방지)
            log.warn("[MessageService] 미정의 메시지 코드: {} ({})", msgCd, langCd);
            return msgCd;
        }
        if (args == null || args.length == 0) {
            return pattern;
        }
        try {
            return MessageFormat.format(pattern, args);
        } catch (IllegalArgumentException e) {
            log.warn("[MessageService] 메시지 포맷 실패 - code={}, pattern={}", msgCd, pattern, e);
            return pattern;
        }
    }

    /** 지정 언어에서 찾고 없으면 기본 언어로 폴백 */
    private String lookup(String langCd, String msgCd) {
        Map<String, String> byLang = cache.get(langCd == null ? DEFAULT_LANG : langCd);
        if (byLang != null && byLang.containsKey(msgCd)) {
            return byLang.get(msgCd);
        }
        Map<String, String> def = cache.get(DEFAULT_LANG);
        return def == null ? null : def.get(msgCd);
    }

    /* ─────────────────────────────────────
       프론트엔드 카탈로그 (langCd 기준 전체 맵)
    ───────────────────────────────────── */
    public Map<String, String> getCatalog(String langCd) {
        Map<String, String> byLang = cache.get(langCd == null ? DEFAULT_LANG : langCd);
        return byLang == null ? Collections.emptyMap() : Map.copyOf(byLang);
    }

    /* ═══════════════════════════════════════
       관리 화면 CRUD
    ═══════════════════════════════════════ */
    @Transactional(readOnly = true)
    public List<MessageDto> findAll(String keyword, String langCd, String msgType) {
        return messageMapper.findAll(
                isBlank(keyword) ? null : keyword,
                isBlank(langCd)  ? null : langCd,
                isBlank(msgType) ? null : msgType);
    }

    @Transactional(readOnly = true)
    public MessageDto findOne(String msgId, String langCd) {
        return messageMapper.findOne(msgId, langCd);
    }

    /** 등록/수정 (Upsert) → 성공 시 캐시 자동 갱신 */
    @Transactional
    public void save(MessageDto dto) {
        if (isBlank(dto.getMsgId()))      throw new CustomException("ERR_REQUIRED", "메시지 코드");
        if (isBlank(dto.getLangCd()))     throw new CustomException("ERR_REQUIRED", "언어 코드");
        if (isBlank(dto.getMsgContent())) throw new CustomException("ERR_REQUIRED", "메시지 본문");

        if (messageMapper.countOne(dto.getMsgId(), dto.getLangCd()) > 0) {
            messageMapper.update(dto);
        } else {
            messageMapper.insert(dto);
        }
        reloadCache();
    }

    @Transactional
    public void updateStatus(String msgId, String langCd, String isUse) {
        messageMapper.updateStatus(msgId, langCd, isUse);
        reloadCache();
    }

    @Transactional
    public void delete(String msgId, String langCd) {
        if (isBlank(msgId) || isBlank(langCd)) throw new CustomException("ERR_REQUIRED", "메시지 코드");
        messageMapper.delete(msgId, langCd);
        reloadCache();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
