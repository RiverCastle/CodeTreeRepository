package com.codetree.CodeTreeHRM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    /**
     * 간단한 로그인 화면 HTML을 직접 반환합니다.
     */
    @GetMapping("/login")
    @ResponseBody
    public String loginPage() {
        return "<html>" +
                "<head><meta charset='UTF-8'></head>" +
                "<body>" +
                "  <h2>로그인 화면 (테스트)</h2>" +
                "  <form action='/login' method='post'>" +
                "    아이디: <input type='text' name='username' placeholder='admin'><br>" +
                "    비밀번호: <input type='password' name='password' placeholder='admin'><br>" +
                "    <button type='submit'>로그인</button>" +
                "  </form>" +
                "</body>" +
                "</html>";
    }

    /**
     * 로그인 요청을 처리합니다.
     * admin / admin 입력 시 /ServerOk 로 이동합니다.
     */
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            // 성공 시 알림창 후 /ServerOk 이동
            return "<script>" +
                    "alert('로그인 성공!'); " +
                    "location.href='/ServerOk';" +
                    "</script>";
        } else {
            // 실패 시 알림창 후 이전 페이지로
            return "<script>" +
                    "alert('아이디 또는 비밀번호가 틀렸습니다.'); " +
                    "history.back();" +
                    "</script>";
        }
    }
}
