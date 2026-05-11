package com.codetree.CodeTreeHRM.controller;

import com.codetree.CodeTreeHRM.dto.MenuSaveRequest;
import com.codetree.CodeTreeHRM.entity.ComMenu;
import com.codetree.CodeTreeHRM.repository.ComMenuRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * 메뉴 관리 REST API 컨트롤러
 *
 * <p>com_menu 테이블에 대한 CRUD API 및 메뉴 코드 자동 채번 API를 제공한다.
 * 모든 API는 세션 인증(loginUser) 체크를 선행한다.
 * 미인증 요청은 HTTP 401 Unauthorized 로 응답한다.</p>
 *
 * <pre>
 * API 목록:
 *   GET    /api/menus          사이드바용 사용 중인 메뉴 목록 (USE_YN='Y')
 *   GET    /api/menus/all      관리화면용 전체 메뉴 목록
 *   POST   /api/menus          메뉴 등록
 *   PUT    /api/menus/{menuCd} 메뉴 수정
 *   DELETE /api/menus/{menuCd} 메뉴 삭제
 *   GET    /api/menus/next-cd  다음 메뉴 코드 자동 채번
 * </pre>
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    /** 메뉴 데이터 접근 Repository */
    private final ComMenuRepository comMenuRepository;

    /**
     * 사이드바용 메뉴 목록 조회
     *
     * <p>USE_YN = 'Y' 인 메뉴만 반환한다.
     * menu_frame.html 의 fetch('/api/menus') 에서 호출한다.</p>
     *
     * @param session 현재 HTTP 세션 (인증 확인용)
     * @return 사용 중인 메뉴 목록 JSON / 미인증 시 401
     */
    @GetMapping("/menus")
    public ResponseEntity<?> getMenus(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(comMenuRepository.findByUseYnOrderByMenuOrdr("Y"));
    }

    /**
     * 관리화면용 전체 메뉴 목록 조회
     *
     * <p>USE_YN 구분 없이 모든 메뉴를 반환한다.
     * menu-mng.html 의 fetch('/api/menus/all') 에서 호출한다.</p>
     *
     * @param session 현재 HTTP 세션 (인증 확인용)
     * @return 전체 메뉴 목록 JSON / 미인증 시 401
     */
    @GetMapping("/menus/all")
    public ResponseEntity<?> getAllMenus(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(comMenuRepository.findAllByOrderByMenuOrdr());
    }

    /**
     * 메뉴 등록
     *
     * <p>요청 바디의 menuCd 가 이미 존재하면 400 Bad Request 로 응답한다.
     * ComMenu.create() 팩토리 메서드를 통해 엔티티를 생성하여 저장한다.</p>
     *
     * @param req     등록할 메뉴 정보 (MenuSaveRequest JSON)
     * @param session 현재 HTTP 세션 (인증 확인용)
     * @return 저장된 ComMenu JSON / 중복 시 400 / 미인증 시 401
     */
    @PostMapping("/menus")
    public ResponseEntity<?> createMenu(@RequestBody MenuSaveRequest req, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 메뉴 코드 중복 체크
        if (comMenuRepository.existsById(req.getMenuCd())) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미 존재하는 메뉴코드입니다."));
        }
        ComMenu menu = ComMenu.create(
            req.getMenuCd(), req.getMenuNm(), req.getMenuIcon(),
            req.getUpperMenuCd(), req.getMenuUrl(),
            req.getMenuOrdr(), req.getUseYn()
        );
        comMenuRepository.save(menu);
        return ResponseEntity.ok(menu);
    }

    /**
     * 메뉴 수정
     *
     * <p>URL PathVariable의 menuCd 로 기존 메뉴를 조회한 후 update() 메서드로 필드를 갱신한다.
     * 메뉴 코드(PK)는 변경 불가하다.</p>
     *
     * @param menuCd  수정할 메뉴 코드 (URL PathVariable)
     * @param req     변경할 메뉴 정보 (MenuSaveRequest JSON)
     * @param session 현재 HTTP 세션 (인증 확인용)
     * @return 수정된 ComMenu JSON / 미존재 시 404 / 미인증 시 401
     */
    @PutMapping("/menus/{menuCd}")
    public ResponseEntity<?> updateMenu(@PathVariable String menuCd,
                                        @RequestBody MenuSaveRequest req,
                                        HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<ComMenu> opt = comMenuRepository.findById(menuCd);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ComMenu menu = opt.get();
        menu.update(req.getMenuNm(), req.getMenuIcon(), req.getUpperMenuCd(),
                    req.getMenuUrl(), req.getMenuOrdr(), req.getUseYn());
        comMenuRepository.save(menu);
        return ResponseEntity.ok(menu);
    }

    /**
     * 메뉴 삭제
     *
     * <p>하위 메뉴 존재 여부는 클라이언트(menu-mng.html)에서 선행 체크한다.
     * 서버에서는 menuCd 존재 여부만 확인한 후 삭제한다.</p>
     *
     * @param menuCd  삭제할 메뉴 코드 (URL PathVariable)
     * @param session 현재 HTTP 세션 (인증 확인용)
     * @return 삭제 완료 메시지 JSON / 미존재 시 404 / 미인증 시 401
     */
    @DeleteMapping("/menus/{menuCd}")
    public ResponseEntity<?> deleteMenu(@PathVariable String menuCd, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!comMenuRepository.existsById(menuCd)) {
            return ResponseEntity.notFound().build();
        }
        comMenuRepository.deleteById(menuCd);
        return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
    }

    /**
     * 다음 메뉴 코드 자동 채번 (레벨별)
     *
     * <p>parentCd 유무에 따라 최상위 또는 하위 메뉴 코드를 채번한다.</p>
     *
     * <pre>
     * 최상위 메뉴 채번 (parentCd 없음):
     *   기존: M001, M002, M003 → 결과: M004
     *   형식: M + 3자리 숫자 (zero-padding)
     *
     * 하위 메뉴 채번 (parentCd 있음):
     *   기존: M001-01, M001-02 → 결과: M001-03
     *   형식: {parentCd} + '-' + 2자리 숫자 (zero-padding)
     * </pre>
     *
     * @param parentCd 상위 메뉴 코드 (최상위 채번 시 생략)
     * @param session  현재 HTTP 세션 (인증 확인용)
     * @return { "menuCd": "M004" } 형태의 JSON / 미인증 시 401
     */
    @GetMapping("/menus/next-cd")
    public ResponseEntity<?> nextMenuCd(@RequestParam(required = false) String parentCd,
                                        HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String nextCd;
        boolean isTopLevel = (parentCd == null || parentCd.isBlank());

        if (isTopLevel) {
            // 최상위 채번: UPPER_MENU_CD가 NULL인 메뉴들의 번호 중 최대값 + 1
            int maxNum = comMenuRepository.findByUpperMenuCdIsNull().stream()
                .mapToInt(m -> {
                    try { return Integer.parseInt(m.getMenuCd().replaceAll("[^0-9]", "")); }
                    catch (Exception e) { return 0; }
                })
                .max().orElse(0);
            nextCd = String.format("M%03d", maxNum + 1);
        } else {
            // 하위 채번: 같은 부모를 가진 형제 메뉴들의 suffix 중 최대값 + 1
            int maxSuffix = comMenuRepository.findByUpperMenuCd(parentCd).stream()
                .mapToInt(m -> {
                    try {
                        String code = m.getMenuCd();
                        int dashIdx = code.lastIndexOf('-');
                        return dashIdx >= 0 ? Integer.parseInt(code.substring(dashIdx + 1)) : 0;
                    } catch (Exception e) { return 0; }
                })
                .max().orElse(0);
            nextCd = String.format("%s-%02d", parentCd, maxSuffix + 1);
        }

        return ResponseEntity.ok(Map.of("menuCd", nextCd));
    }
}
