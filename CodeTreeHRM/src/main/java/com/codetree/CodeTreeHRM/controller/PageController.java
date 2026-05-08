package com.codetree.CodeTreeHRM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * DB 메뉴 URL → 정적 HTML 파일 포워딩
 * com_menu.MENU_URL 값이 변경되면 이 파일도 함께 업데이트합니다.
 */
@Controller
public class PageController {

    // ── 공통관리 ──────────────────────────────────────────
    @GetMapping("/system/menu")
    public String menuPage() {
        return "forward:/pages/menu.html";
    }

    @GetMapping("/system/user")
    public String userPage() {
        return "forward:/pages/user.html";
    }

    @GetMapping("/system/role")
    public String rolePage() {
        return "forward:/pages/role.html";
    }

    @GetMapping("/system/login-history")
    public String loginHistoryPage() {
        return "forward:/pages/login-history.html";
    }

    // ── 공통코드 ──────────────────────────────────────────
    @GetMapping("/common/code")
    public String commonCodePage() {
        return "forward:/pages/common-code.html";
    }

    @GetMapping("/common/message")
    public String commonMessagePage() {
        return "forward:/pages/common-message.html";
    }

    // ── 사원관리 ──────────────────────────────────────────
    @GetMapping("/hr/appointment")
    public String hrAppointmentPage() {
        return "forward:/pages/hr-appointment.html";
    }

    @GetMapping("/hr/manage")
    public String hrManagePage() {
        return "forward:/pages/hr-manage.html";
    }

    // ── 부서관리 ──────────────────────────────────────────
    @GetMapping("/dept/manage")
    public String deptManagePage() {
        return "forward:/pages/dept-manage.html";
    }
}
