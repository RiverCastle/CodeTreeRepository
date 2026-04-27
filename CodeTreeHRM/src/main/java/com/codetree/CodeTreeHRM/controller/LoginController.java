package com.codetree.CodeTreeHRM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    /**
     * 메인 루트(/) 접속 시 로그인 페이지로 리다이렉트합니다.
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    /**
     * 세련된 로그인 화면 HTML을 반환합니다.
     */
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
                "        body { font-family: sans-serif; background-color: var(--bg); display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                "        .login-box { background: white; padding: 40px; border-radius: 20px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 350px; text-align: center; }" +
                "        .logo-img { width: 60px; margin-bottom: 10px; }" +
                "        .logo-text { font-size: 1.5rem; font-weight: 800; color: var(--primary); margin-bottom: 30px; }" +
                "        .logo-text span { color: var(--accent); font-weight: 300; }" +
                "        input { width: 100%; padding: 12px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }" +
                "        button { width: 100%; padding: 12px; background: var(--primary); color: white; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; transition: 0.3s; }" +
                "        button:hover { background: var(--accent); }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='login-box'>" +
                "        <img src='/assets/logo/logo.svg' class='logo-img' alt='Logo'>" +
                "        <div class='logo-text'>CODE<span>TREE</span></div>" +
                "        <form action='/login' method='post'>" +
                "            <input type='text' name='username' placeholder='아이디' required>" +
                "            <input type='password' name='password' placeholder='비밀번호' required>" +
                "            <button type='submit'>로그인</button>" +
                "        </form>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return "<script>alert('로그인 성공!'); location.href='/main';</script>";
        } else {
            return "<script>alert('아이디 또는 비밀번호가 틀렸습니다.'); history.back();</script>";
        }
    }

    /**
     * 로그인 성공 후 이동할 메인 페이지 (기존 index.html 내용 활용 가능)
     */
    @GetMapping("/main")
    public String mainPage() {
        return "forward:/index.html";
    }
}
