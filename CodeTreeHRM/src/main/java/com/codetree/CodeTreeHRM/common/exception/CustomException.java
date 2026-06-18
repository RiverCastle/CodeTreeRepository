package com.codetree.CodeTreeHRM.common.exception;

import lombok.Getter;

import java.util.List;

/**
 * 비즈니스 로직 전반에서 사용하는 공통 커스텀 예외.
 * <p>
 * 메시지 본문을 직접 들고 다니지 않고 <b>메시지 코드(msgCd)</b>와 <b>치환 파라미터(args)</b>만 보관한다.
 * 실제 문장 완성은 {@link com.codetree.CodeTreeHRM.message.service.MessageService} 가
 * {@code @RestControllerAdvice} 단계에서 수행한다.
 *
 * <pre>
 *   throw new CustomException("ERR_REQUIRED", "사원번호");
 *   throw new CustomException("ERR_DUPLICATE", empNo);
 * </pre>
 */
@Getter
public class CustomException extends RuntimeException {

    /** com_message.msg_id 와 매핑되는 메시지 코드 */
    private final String msgCd;

    /** MessageFormat 치환 파라미터 ({0}, {1} ...) */
    private final transient Object[] args;

    public CustomException(String msgCd, Object... args) {
        super(msgCd);
        this.msgCd = msgCd;
        this.args = args;
    }

    public CustomException(String msgCd, List<Object> args) {
        this(msgCd, args == null ? new Object[0] : args.toArray());
    }
}
