/**
 * common.js — CodeTreeHRM 공통 스크립트
 *
 * 모든 업무 화면에서 공통으로 사용하는 UI 유틸리티 모음.
 * 이 파일 하나를 <script src="/common.js"> 로 포함하면
 * 아래 기능이 즉시 활성화된다.
 *
 * ┌─────────────────────────────────────────────────────────────┐
 * │  제공 기능                                                   │
 * │  1) Alert 모달     — 확인 버튼 1개 (openAlertModal)          │
 * │  2) Confirm 모달   — 확인 / 취소 버튼 (openConfirmModal)     │
 * │  3) Dirty 모달     — 저장 후 이동 / 저장 안 함 / 취소        │
 * │                      (openDirtyModal)                       │
 * │  4) 토스트 알림    — 화면 하단 중앙 2.6초 (showToast)        │
 * │  5) HTML 이스케이프 유틸 — XSS 방지 (escHtml)               │
 * └─────────────────────────────────────────────────────────────┘
 *
 * [모달 사용 패턴]
 *   모든 모달 함수는 Promise 를 반환한다.
 *   await 와 함께 사용해야 응답을 얻을 수 있다.
 *
 *   // Alert: 단순 알림 (확인 후 계속)
 *   await openAlertModal({ icon:'⚠️', title:'오류', desc:'메시지' });
 *
 *   // Confirm: 예/아니오 분기
 *   const ok = await openConfirmModal({ title:'삭제', desc:'삭제하시겠습니까?' });
 *   if (ok) { ... }
 *
 *   // Dirty: 변경감지 — 3가지 선택지
 *   const action = await openDirtyModal();
 *   // action === 'save'    → 저장 후 이동 선택
 *   // action === 'discard' → 저장 안 함(버리기) 선택
 *   // action === 'cancel'  → 취소 (이동하지 않음) 선택
 *
 * [HTML 주입 방식]
 *   IIFE(즉시실행함수) 안에서 DOMContentLoaded 이벤트를 통해
 *   <body> 끝에 모달 3개 + 토스트 1개를 자동으로 삽입한다.
 *   별도로 HTML 마크업을 작성할 필요가 없다.
 */

/* ════════════════════════════════════════════════════════════════
   § 1. 공통 HTML 동적 주입
   ────────────────────────────────────────────────────────────────
   페이지 로드 시 <body> 끝에 모달 + 토스트 HTML 을 자동 삽입한다.
   IIFE 패턴을 사용해 전역 스코프 오염을 최소화한다.
   insertAdjacentHTML('beforeend', ...) 을 쓰면 기존 DOM 을 건드리지 않고
   <body> 마지막 자식으로 삽입할 수 있다.
════════════════════════════════════════════════════════════════ */
(function injectCommonHtml() {
  /**
   * 주입할 HTML 문자열.
   * 세 종류의 모달과 토스트 컨테이너를 한 번에 정의한다.
   *
   * - alertModal   : 확인 버튼 1개 — 단순 알림용
   * - confirmModal : 확인 + 취소 버튼 — 예/아니오 선택용
   * - dirtyModal   : 저장 후 이동 / 저장 안 함 / 취소 — 변경감지 이동 전 확인용
   * - toast        : 화면 하단 중앙 팝업 메시지
   *
   * 각 모달의 내용(아이콘·제목·설명·버튼 텍스트)은 openXxxModal() 호출 시
   * JavaScript 로 동적으로 채운다.
   */
  const html = `
    <!-- ── Alert 모달: 확인 버튼 1개 ── -->
    <div class="modal-backdrop" id="alertModal">
      <div class="modal-box">
        <div class="modal-icon"  id="alertModalIcon">ℹ️</div>
        <div class="modal-title" id="alertModalTitle"></div>
        <div class="modal-desc"  id="alertModalDesc"></div>
        <div class="modal-btns">
          <!-- onclick 으로 resolveAlertModal() 직접 호출 -->
          <button class="modal-btn modal-btn-primary" id="alertModalOkBtn" onclick="resolveAlertModal()">확인</button>
        </div>
      </div>
    </div>

    <!-- ── Confirm 모달: 확인 + 취소 버튼 ── -->
    <div class="modal-backdrop" id="confirmModal">
      <div class="modal-box">
        <div class="modal-icon"  id="confirmModalIcon">⚠️</div>
        <div class="modal-title" id="confirmModalTitle"></div>
        <div class="modal-desc"  id="confirmModalDesc"></div>
        <div class="modal-btns">
          <!-- 확인 → true, 취소 → false 를 Promise 로 전달 -->
          <button class="modal-btn modal-btn-primary" id="confirmModalOkBtn" onclick="resolveConfirmModal(true)">확인</button>
          <button class="modal-btn modal-btn-default" onclick="resolveConfirmModal(false)">취소</button>
        </div>
      </div>
    </div>

    <!-- ── Dirty 모달: 저장 후 이동 / 저장 안 함 / 취소 ── -->
    <div class="modal-backdrop" id="dirtyModal">
      <div class="modal-box">
        <div class="modal-icon">💾</div>
        <div class="modal-title">저장하지 않은 변경사항이 있습니다</div>
        <div class="modal-desc">다른 항목으로 이동하기 전에<br>변경사항을 저장하시겠습니까?</div>
        <div class="modal-btns">
          <!-- 각 버튼은 action 문자열('save'|'discard'|'cancel')을 전달 -->
          <button class="modal-btn modal-btn-primary" onclick="resolveDirtyModal('save')">저장 후 이동</button>
          <button class="modal-btn modal-btn-default" onclick="resolveDirtyModal('discard')">저장 안 함</button>
          <button class="modal-btn modal-btn-danger"  onclick="resolveDirtyModal('cancel')">취소</button>
        </div>
      </div>
    </div>

    <!-- ── 토스트 컨테이너 ── -->
    <!-- showToast() 호출 시 텍스트와 클래스를 동적으로 설정한다 -->
    <div class="toast" id="toast"></div>
  `;

  /**
   * DOMContentLoaded 이후에 삽입해야 <body> 요소가 존재한다.
   * document.body 가 준비된 시점에 한 번만 실행된다.
   */
  document.addEventListener('DOMContentLoaded', () => {
    document.body.insertAdjacentHTML('beforeend', html);
  });
})();


/* ════════════════════════════════════════════════════════════════
   § 2. Alert 모달 (확인 버튼 1개)
   ────────────────────────────────────────────────────────────────
   단순 알림/오류 안내용 모달.
   사용자가 확인 버튼을 누를 때까지 await 로 대기한다.
════════════════════════════════════════════════════════════════ */

/**
 * openAlertModal 의 Promise resolve 콜백을 보관하는 변수.
 * 모달이 열려있는 동안 null 이 아닌 함수가 저장되며,
 * resolveAlertModal() 호출 시 한 번 실행 후 null 로 초기화된다.
 * @type {Function|null}
 */
let _alertResolve = null;

/**
 * Alert 모달을 열고, 사용자가 확인을 누를 때까지 대기한다.
 *
 * @param {object} [opts] 모달 옵션
 * @param {string} [opts.icon='ℹ️']                아이콘 이모지
 * @param {string} [opts.title='']                 제목 (textContent 로 표시)
 * @param {string} [opts.desc='']                  설명 (innerHTML 로 삽입 — HTML 태그 사용 가능)
 * @param {string} [opts.okText='확인']             확인 버튼 텍스트
 * @param {string} [opts.okClass='modal-btn-primary'] 확인 버튼 CSS 클래스
 * @returns {Promise<void>} 확인 버튼 클릭 시 resolve 되는 Promise
 *
 * @example
 * // 오류 알림 (빨간 버튼)
 * await openAlertModal({ icon:'🚫', title:'오류', desc:'처리 중 오류가 발생했습니다.', okClass:'modal-btn-danger' });
 *
 * @example
 * // 성공 알림
 * await openAlertModal({ icon:'✅', title:'저장 완료', desc:'메뉴가 저장되었습니다.' });
 */
function openAlertModal({ icon = 'ℹ️', title = '', desc = '', okText = '확인', okClass = 'modal-btn-primary' } = {}) {
  // 모달 내부 텍스트/HTML 갱신
  document.getElementById('alertModalIcon').textContent  = icon;
  document.getElementById('alertModalTitle').textContent = title;
  document.getElementById('alertModalDesc').innerHTML    = desc;   // HTML 허용 (줄바꿈 등)

  // 확인 버튼 텍스트 및 색상 클래스 설정
  const okBtn = document.getElementById('alertModalOkBtn');
  okBtn.textContent = okText;
  okBtn.className   = `modal-btn ${okClass}`;   // modal-btn-primary / modal-btn-danger 등

  // 모달 표시 (.show 클래스 추가 → common.css: display: flex)
  document.getElementById('alertModal').classList.add('show');

  // Promise 생성 후 resolve 함수를 모듈 변수에 보관
  // resolveAlertModal() 이 호출되면 이 Promise 가 완료된다
  return new Promise(resolve => { _alertResolve = resolve; });
}

/**
 * Alert 모달을 닫고 대기 중인 Promise 를 resolve 한다.
 * HTML 버튼의 onclick="resolveAlertModal()" 에서 직접 호출된다.
 * 이중 호출 방지를 위해 실행 후 _alertResolve 를 null 로 초기화한다.
 */
function resolveAlertModal() {
  document.getElementById('alertModal').classList.remove('show');  // 모달 숨김
  if (_alertResolve) {
    _alertResolve();        // await 를 풀어줌
    _alertResolve = null;   // 메모리 누수 방지 및 이중 호출 방지
  }
}


/* ════════════════════════════════════════════════════════════════
   § 3. Confirm 모달 (확인 / 취소 버튼)
   ────────────────────────────────────────────────────────────────
   예/아니오 선택용 모달.
   확인 클릭 → Promise<true>, 취소 클릭 → Promise<false>
════════════════════════════════════════════════════════════════ */

/**
 * openConfirmModal 의 Promise resolve 콜백 저장 변수.
 * @type {Function|null}
 */
let _confirmResolve = null;

/**
 * Confirm 모달을 열고, 사용자 선택(확인/취소)을 비동기로 반환한다.
 *
 * @param {object} [opts] 모달 옵션
 * @param {string} [opts.icon='⚠️']                아이콘 이모지
 * @param {string} [opts.title='']                 제목
 * @param {string} [opts.desc='']                  설명 (HTML 허용)
 * @param {string} [opts.okText='확인']             확인 버튼 텍스트
 * @param {string} [opts.okClass='modal-btn-primary'] 확인 버튼 CSS 클래스
 * @returns {Promise<boolean>} 확인 → true, 취소 → false
 *
 * @example
 * const ok = await openConfirmModal({
 *   icon:    '🗑️',
 *   title:   '삭제 확인',
 *   desc:    '정말 삭제하시겠습니까?',
 *   okText:  '삭제',
 *   okClass: 'modal-btn-danger',
 * });
 * if (ok) { // 삭제 처리 }
 */
function openConfirmModal({ icon = '⚠️', title = '', desc = '', okText = '확인', okClass = 'modal-btn-primary' } = {}) {
  // 동적 콘텐츠 채우기
  document.getElementById('confirmModalIcon').textContent  = icon;
  document.getElementById('confirmModalTitle').textContent = title;
  document.getElementById('confirmModalDesc').innerHTML    = desc;

  // 확인 버튼 텍스트와 스타일 적용
  const okBtn = document.getElementById('confirmModalOkBtn');
  okBtn.textContent = okText;
  okBtn.className   = `modal-btn ${okClass}`;

  // 모달 표시
  document.getElementById('confirmModal').classList.add('show');

  // 선택 결과를 resolve 콜백에 저장하고 Promise 반환
  return new Promise(resolve => { _confirmResolve = resolve; });
}

/**
 * Confirm 모달을 닫고 선택 결과를 Promise 로 전달한다.
 * HTML 버튼의 onclick="resolveConfirmModal(true/false)" 에서 호출된다.
 *
 * @param {boolean} result 확인(true) 또는 취소(false)
 */
function resolveConfirmModal(result) {
  document.getElementById('confirmModal').classList.remove('show');
  if (_confirmResolve) {
    _confirmResolve(result);  // true 또는 false 를 awaiter 에 전달
    _confirmResolve = null;
  }
}


/* ════════════════════════════════════════════════════════════════
   § 4. Dirty 모달 (변경감지 이동 확인)
   ────────────────────────────────────────────────────────────────
   폼에 미저장 변경사항이 있을 때 다른 항목으로 이동하기 전
   사용자에게 의사를 묻는 3-버튼 모달.

   반환값(string):
     'save'    → 저장 후 이동 선택 → 호출측에서 saveMenu() 실행 후 이동
     'discard' → 저장 안 함 선택  → 변경사항 무시하고 이동
     'cancel'  → 취소 선택        → 이동 중단 (현재 화면 유지)
════════════════════════════════════════════════════════════════ */

/**
 * openDirtyModal 의 Promise resolve 콜백 저장 변수.
 * @type {Function|null}
 */
let _dirtyResolve = null;

/**
 * 변경감지 이동 확인 모달을 열고 사용자 선택을 반환한다.
 * 모달 내 제목·설명은 고정 텍스트이므로 옵션 파라미터가 없다.
 *
 * @returns {Promise<'save'|'discard'|'cancel'>}
 *
 * @example
 * const action = await openDirtyModal();
 * if (action === 'cancel') return;          // 이동 취소
 * if (action === 'save') await saveMenu();  // 저장 후 이동
 * // 'discard' 또는 저장 완료 후 → 이동 실행
 */
function openDirtyModal() {
  document.getElementById('dirtyModal').classList.add('show');
  return new Promise(resolve => { _dirtyResolve = resolve; });
}

/**
 * Dirty 모달을 닫고 사용자가 선택한 action 을 Promise 로 전달한다.
 * HTML 버튼의 onclick="resolveDirtyModal('save')" 등에서 호출된다.
 *
 * @param {'save'|'discard'|'cancel'} action 버튼에 대응하는 액션 문자열
 */
function resolveDirtyModal(action) {
  document.getElementById('dirtyModal').classList.remove('show');
  if (_dirtyResolve) {
    _dirtyResolve(action);  // 'save' | 'discard' | 'cancel' 전달
    _dirtyResolve = null;
  }
}


/* ════════════════════════════════════════════════════════════════
   § 5. 토스트 알림
   ────────────────────────────────────────────────────────────────
   화면 하단 중앙에 2.6초간 표시 후 자동으로 사라지는 메시지.
   현재는 openAlertModal 로 대체되어 사용 빈도가 낮지만,
   즉각적·비침습적 피드백이 필요한 경우를 위해 유지한다.
════════════════════════════════════════════════════════════════ */

/**
 * 자동 숨김 타이머 ID.
 * 연속 호출 시 이전 타이머를 clearTimeout 으로 취소해
 * 메시지 표시 시간이 누적되지 않도록 한다.
 * @type {number|undefined}
 */
let _toastTimer;

/**
 * 하단 토스트 메시지를 표시한다.
 * 2600ms 후 자동으로 사라진다.
 *
 * @param {string}               msg  표시할 메시지 텍스트
 * @param {'success'|'error'|''} type 색상 타입
 *                                    'success' → 녹색(#82A67D)
 *                                    'error'   → 빨간색(#E74C3C)
 *                                    ''        → 기본 어두운 색(#34495E)
 *
 * @example
 * showToast('저장되었습니다.', 'success');
 * showToast('오류가 발생했습니다.', 'error');
 */
function showToast(msg, type) {
  const t = document.getElementById('toast');
  if (!t) return;                              // 토스트 요소가 아직 주입 전이면 skip

  t.textContent = msg;
  t.className   = 'toast show ' + (type || ''); // 'toast show success' 등

  clearTimeout(_toastTimer);                   // 이전 타이머 취소 (연속 호출 대응)
  _toastTimer = setTimeout(() => {
    t.className = 'toast';                     // .show 제거 → opacity 0 으로 페이드아웃
  }, 2600);
}


/* ════════════════════════════════════════════════════════════════
   § 6. HTML 이스케이프 유틸
   ────────────────────────────────────────────────────────────────
   사용자 입력값을 innerHTML 로 삽입하기 전 반드시 escHtml() 을 통해
   특수문자를 HTML 엔티티로 변환해 XSS 를 방지한다.
════════════════════════════════════════════════════════════════ */

/**
 * 문자열의 HTML 특수문자를 엔티티로 변환한다 (XSS 방지).
 *
 * 변환 대상:
 *   &  → &amp;
 *   "  → &quot;
 *   <  → &lt;
 *   >  → &gt;
 *
 * @param {string|null|undefined} s 변환할 문자열 (null/undefined 안전 처리됨)
 * @returns {string} 이스케이프된 문자열
 *
 * @example
 * escHtml('<script>alert(1)</script>') // → '&lt;script&gt;alert(1)&lt;/script&gt;'
 * escHtml(null)                        // → ''
 */
function escHtml(s) {
  return (s || '')
    .replace(/&/g,  '&amp;')   // & 를 먼저 처리 (다른 엔티티의 & 가 이중 변환되지 않도록)
    .replace(/"/g,  '&quot;')
    .replace(/</g,  '&lt;')
    .replace(/>/g,  '&gt;');
}
