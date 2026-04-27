package com.codetree.CodeTreeHRM.controller;

import com.codetree.CodeTreeHRM.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    @ResponseBody
    public String loginPage() {
        return "<!DOCTYPE html>" +
                "<html lang='ko'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <title>로그인 - CodeTree HRM</title>" +
                "    <script src='https://code.jquery.com/jquery-3.7.1.min.js'></script>" +
                "    <style>" +
                "        :root { --primary: #2C3E50; --accent: #82A67D; --bg: #F1F2F6; }" +
                "        * { box-sizing: border-box; margin: 0; padding: 0; }" +
                "        body { font-family: sans-serif; background-color: var(--bg); display: flex; justify-content: center; align-items: center; height: 100vh; }" +
                "        .login-box { background: white; padding: 40px; border-radius: 20px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 350px; text-align: center; }" +
                "        .logo-img { width: 60px; margin-bottom: 10px; }" +
                "        .logo-text { font-size: 1.5rem; font-weight: 800; color: var(--primary); margin-bottom: 30px; }" +
                "        .logo-text span { color: var(--accent); font-weight: 300; }" +
                "        input { width: 100%; padding: 12px; margin-bottom: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; }" +
                "        input:focus { outline: none; border-color: var(--accent); }" +
                "        button { width: 100%; padding: 12px; background: var(--primary); color: white; border: none; border-radius: 8px; font-weight: 600; font-size: 15px; cursor: pointer; transition: background 0.3s; }" +
                "        button:hover { background: var(--accent); }" +
                "        .error-msg { color: #e74c3c; font-size: 13px; margin-bottom: 10px; display: none; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='login-box'>" +
                "        <img src='/assets/logo/logo.svg' class='logo-img' alt='Logo'>" +
                "        <div class='logo-text'>CODE<span>TREE</span></div>" +
                "        <div class='error-msg' id='errorMsg'></div>" +
                "        <form id='loginForm'>" +
                "            <input type='text' id='userId' placeholder='아이디' required>" +
                "            <input type='password' id='userPwd' placeholder='비밀번호' required>" +
                "            <button type='submit'>로그인</button>" +
                "        </form>" +
                "    </div>" +
                "    <script>" +
                "        $('#loginForm').on('submit', function(e) {" +
                "            e.preventDefault();" +
                "            $('#errorMsg').hide();" +
                "            $.post('/login', { userId: $('#userId').val(), userPwd: $('#userPwd').val() })" +
                "                .done(function(res) {" +
                "                    if (res.success) location.href = '/main';" +
                "                    else $('#errorMsg').text(res.message).show();" +
                "                })" +
                "                .fail(function() { $('#errorMsg').text('서버 오류가 발생했습니다.').show(); });" +
                "        });" +
                "    </script>" +
                "</body>" +
                "</html>";
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam String userId, @RequestParam String userPwd, HttpSession session) {
        try {
            loginService.authenticate(userId, userPwd);
            session.setAttribute("userId", userId);
            return Map.of("success", true);
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @GetMapping("/api/me")
    @ResponseBody
    public Map<String, Object> getCurrentUser(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return Map.of("loggedIn", false);
        return Map.of("loggedIn", true, "userId", userId);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "forward:/index.html";
    }
}