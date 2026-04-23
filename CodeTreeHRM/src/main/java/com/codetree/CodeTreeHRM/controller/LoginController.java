package com.codetree.CodeTreeHRM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping("/")
    public String root() {
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
                "        * { box-sizing: border-box; }" +
                "        body { font-family: sans-serif; background: var(--bg); display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                "        .login-box { background: white; padding: 40px; border-radius: 20px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 350px; text-align: center; }" +
                "        .logo-img { width: 60px; margin-bottom: 10px; }" +
                "        .logo-text { font-size: 1.5rem; font-weight: 800; color: var(--primary); margin-bottom: 30px; }" +
                "        .logo-text span { color: var(--accent); font-weight: 300; }" +
                "        input { width: 100%; padding: 12px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 8px; }" +
                "        button { width: 100%; padding: 12px; background: var(--primary); color: white; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; transition: 0.3s; }" +
                "        button:hover { background: var(--accent); }" +
                "        button:disabled { opacity: 0.6; cursor: default; }" +
                "        .error-msg { color: #e74c3c; font-size: 0.85rem; margin-bottom: 12px; display: none; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='login-box'>" +
                "        <img src='/assets/logo/logo.svg' class='logo-img' alt='Logo'>" +
                "        <div class='logo-text'>CODE<span>TREE</span></div>" +
                "        <form id='loginForm'>" +
                "            <input type='text' id='userId' placeholder='아이디' required autocomplete='username'>" +
                "            <input type='password' id='password' placeholder='비밀번호' required autocomplete='current-password'>" +
                "            <div class='error-msg' id='errorMsg'></div>" +
                "            <button type='submit' id='loginBtn'>로그인</button>" +
                "        </form>" +
                "    </div>" +
                "    <script>" +
                "        $('#loginForm').on('submit', function(e) {" +
                "            e.preventDefault();" +
                "            var btn = $('#loginBtn');" +
                "            btn.prop('disabled', true).text('로그인 중...');" +
                "            $('#errorMsg').hide();" +
                "            $.ajax({" +
                "                url: '/api/auth/login'," +
                "                method: 'POST'," +
                "                contentType: 'application/json'," +
                "                data: JSON.stringify({ userId: $('#userId').val(), password: $('#password').val() })," +
                "                success: function(res) {" +
                "                    if (res.success) {" +
                "                        localStorage.setItem('accessToken', res.data.accessToken);" +
                "                        localStorage.setItem('userId', res.data.userId);" +
                "                        localStorage.setItem('empNm', res.data.empNm);" +
                "                        location.href = '/main';" +
                "                    }" +
                "                }," +
                "                error: function(xhr) {" +
                "                    var msg = (xhr.responseJSON && xhr.responseJSON.message) ? xhr.responseJSON.message : '로그인에 실패했습니다.';" +
                "                    $('#errorMsg').text(msg).show();" +
                "                    btn.prop('disabled', false).text('로그인');" +
                "                }" +
                "            });" +
                "        });" +
                "    </script>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "forward:/index.html";
    }
}
