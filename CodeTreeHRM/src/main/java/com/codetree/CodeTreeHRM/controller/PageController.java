package com.codetree.CodeTreeHRM.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 화면 라우팅 컨트롤러
 *
 * <p>com_menu 테이블의 MENU_URL 컬럼 값을 기준으로
 * 각 URL 요청을 실제 정적 HTML 파일로 포워딩(forward)한다.
 * 모든 요청은 세션 인증을 먼저 확인하며, 미인증 시 로그인 페이지로 리다이렉트한다.</p>
 *
 * <pre>
 * 포워딩 방식을 사용하는 이유:
 *   - 브라우저 URL은 DB의 MENU_URL(/system/menu 등)을 유지
 *   - 실제 파일 경로(menu-mng.html 등)는 숨길 수 있음
 *   - DB URL 변경 시 이 컨트롤러만 수정하면 됨
 * </pre>
 */
@Controller
public class PageController {

    /* ──────────────────────────────────────
       /system/** 시스템 관리 영역
       com_menu: M004 시스템관리 하위 메뉴
    ────────────────────────────────────── */

    /**
     * 메뉴 관리 화면
     * MENU_URL: /system/menu → menu-mng.html
     */
    @GetMapping("/system/menu")
    public String menuPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/menu-mng.html";
    }

    /**
     * 사용자 관리 화면
     * MENU_URL: /system/user → placeholder.html (미구현)
     */
    @GetMapping("/system/user")
    public String userPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /**
     * 권한 관리 화면
     * MENU_URL: /system/role → placeholder.html (미구현)
     */
    @GetMapping("/system/role")
    public String rolePage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /**
     * 로그인 이력 화면
     * MENU_URL: /system/login-history → placeholder.html (미구현)
     */
    @GetMapping("/system/login-history")
    public String loginHistoryPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /* ──────────────────────────────────────
       /hr/** 인사 관리 영역
       com_menu: M001 사원관리 하위 메뉴
    ────────────────────────────────────── */

    /**
     * 인사발령 화면
     * MENU_URL: /hr/appointment → placeholder.html (미구현)
     */
    @GetMapping("/hr/appointment")
    public String appointmentPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /**
     * 인사정보 화면
     * MENU_URL: /hr/info → placeholder.html (미구현)
     */
    @GetMapping("/hr/info")
    public String hrInfoPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /**
     * 사원등록 화면
     * MENU_URL: /hr/register → placeholder.html (미구현)
     */
    @GetMapping("/hr/register")
    public String hrRegisterPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /* ──────────────────────────────────────
       /dept/** 부서 관리 영역
       com_menu: M002 부서관리 하위 메뉴
    ────────────────────────────────────── */

    /**
     * 부서 목록 화면
     * MENU_URL: /dept/list → placeholder.html (미구현)
     */
    @GetMapping("/dept/list")
    public String deptListPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /**
     * 부서 등록 화면
     * MENU_URL: /dept/register → placeholder.html (미구현)
     */
    @GetMapping("/dept/register")
    public String deptRegisterPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /* ──────────────────────────────────────
       /common/** 공통코드 관리 영역
       com_menu: M003 공통코드관리 하위 메뉴
    ────────────────────────────────────── */

    /**
     * 공통코드 목록 화면
     * MENU_URL: /common/code/list → placeholder.html (미구현)
     */
    @GetMapping("/common/code/list")
    public String commonCodeListPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /**
     * 공통코드 등록 화면
     * MENU_URL: /common/code/register → placeholder.html (미구현)
     */
    @GetMapping("/common/code/register")
    public String commonCodeRegisterPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }
}
