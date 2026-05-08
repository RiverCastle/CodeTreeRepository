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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final ComMenuRepository comMenuRepository;

    /** 사이드바용 - USE_YN='Y' 메뉴만 */
    @GetMapping("/menus")
    public ResponseEntity<?> getMenus(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(comMenuRepository.findByUseYnOrderByMenuOrdr("Y"));
    }

    /** 관리화면용 - 전체 메뉴 */
    @GetMapping("/menus/all")
    public ResponseEntity<?> getAllMenus(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(comMenuRepository.findAllByOrderByMenuOrdr());
    }

    /** 메뉴 등록 */
    @PostMapping("/menus")
    public ResponseEntity<?> createMenu(@RequestBody MenuSaveRequest req, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

    /** 메뉴 수정 */
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

    /** 메뉴 삭제 */
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

    /** 다음 메뉴코드 자동생성 (레벨별 채번) */
    @GetMapping("/menus/next-cd")
    public ResponseEntity<?> nextMenuCd(@RequestParam(required = false) String parentCd,
                                        HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String nextCd;
        boolean isTopLevel = (parentCd == null || parentCd.isBlank());

        if (isTopLevel) {
            // 최상위: M001, M002, M003 ... → max + 1
            int maxNum = comMenuRepository.findByUpperMenuCdIsNull().stream()
                .mapToInt(m -> {
                    try { return Integer.parseInt(m.getMenuCd().replaceAll("[^0-9]", "")); }
                    catch (Exception e) { return 0; }
                })
                .max().orElse(0);
            nextCd = String.format("M%03d", maxNum + 1);
        } else {
            // 하위: {parentCd}-01, {parentCd}-02 ... → 형제 중 max suffix + 1
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
