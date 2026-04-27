package com.codetree.CodeTreeHRM.controller;

import com.codetree.CodeTreeHRM.entity.ComUser;
import com.codetree.CodeTreeHRM.repository.ComUserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private static final int MAX_LOGIN_FAIL = 5;

    private final ComUserRepository comUserRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
                "    <style>" +
                "        :root { --primary: #2C3E50; --accent: #82A67D; --bg: #F1F2F6; }" +
                "        body { font-family: sans-serif; background-color: var(--bg); display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                "        .login-box { background: white; padding: 40px; border-radius: 20px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 350px; text-align: center; }" +
                "        .logo-img { width: 60px; margin-bottom: 10px; }" +
                "        .logo-text { font-size: 1.5rem; font-weight: 800; color: var(--primary); margin-bottom: 30px; }" +
                "        .logo-text span { color: var(--accent); font-weight: 300; }" +
                "        input { width: 100%; padding: 12px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; font-size: 0.95rem; }" +
                "        button { width: 100%; padding: 12px; background: var(--primary); color: white; border: none; border-radius: 8px; font-weight: 600; font-size: 0.95rem; cursor: pointer; transition: 0.3s; }" +
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
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        Optional<ComUser> userOpt = comUserRepository.findByUserId(username);

        if (userOpt.isEmpty()) {
            return fail("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        ComUser user = userOpt.get();

        if ("N".equals(user.getUseYn())) {
            return fail("사용이 중지된 계정입니다. 관리자에게 문의하세요.");
        }

        if ("Y".equals(user.getAcctLockYn())) {
            return fail("잠긴 계정입니다. 관리자에게 문의하세요.");
        }

        if (!passwordEncoder.matches(password, user.getUserPwd())) {
            int newFailCount = user.getLoginFailAtmtCnt() + 1;
            comUserRepository.incrementLoginFailCount(user.getUserNo());

            if (newFailCount >= MAX_LOGIN_FAIL) {
                comUserRepository.lockAccount(user.getUserNo());
                return fail("로그인 " + MAX_LOGIN_FAIL + "회 실패로 계정이 잠겼습니다. 관리자에게 문의하세요.");
            }

            return fail("아이디 또는 비밀번호가 올바르지 않습니다. (" + newFailCount + "/" + MAX_LOGIN_FAIL + ")");
        }

        comUserRepository.resetOnLoginSuccess(user.getUserNo());
        session.setAttribute("loginUser", user.getUserId());
        session.setAttribute("loginUserNo", user.getUserNo());
        return "<script>location.href='/main';</script>";
    }

    @GetMapping("/main")
    public String mainPage(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        return "forward:/index.html";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String fail(String message) {
        return "<script>alert('" + message + "'); history.back();</script>";
    }
}
