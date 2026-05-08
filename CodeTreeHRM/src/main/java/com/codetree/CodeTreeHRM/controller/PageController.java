package com.codetree.CodeTreeHRM.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 화면 URL → 정적 HTML 페이지 라우팅 컨트롤러
 * DB com_menu.MENU_URL 기준으로 매핑
 */
@Controller
public class PageController {

    /* ──────────────────────────────────────
       /system/** 시스템 관리 영역
    ────────────────────────────────────── */
    @GetMapping("/system/menu")
    public String menuPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/menu-mng.html";
    }

    @GetMapping("/system/user")
    public String userPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    @GetMapping("/system/role")
    public String rolePage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    @GetMapping("/system/login-history")
    public String loginHistoryPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /* ──────────────────────────────────────
       /hr/** 인사 관리 영역
    ────────────────────────────────────── */
    @GetMapping("/hr/appointment")
    public String appointmentPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    @GetMapping("/hr/info")
    public String hrInfoPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    @GetMapping("/hr/register")
    public String hrRegisterPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /* ──────────────────────────────────────
       /dept/** 부서 관리 영역
    ────────────────────────────────────── */
    @GetMapping("/dept/list")
    public String deptListPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    @GetMapping("/dept/register")
    public String deptRegisterPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    /* ──────────────────────────────────────
       /common/** 공통 코드 영역
    ────────────────────────────────────── */
    @GetMapping("/common/code/list")
    public String commonCodeListPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }

    @GetMapping("/common/code/register")
    public String commonCodeRegisterPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) return "redirect:/login";
        return "forward:/placeholder.html";
    }
}
