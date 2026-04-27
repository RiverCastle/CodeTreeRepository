package com.codetree.CodeTreeHRM.service;

import com.codetree.CodeTreeHRM.entity.ComUser;
import com.codetree.CodeTreeHRM.repository.ComUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final ComUserRepository comUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ComUser authenticate(String userId, String password) {
        ComUser user = comUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if ("N".equals(user.getUseYn())) {
            throw new IllegalStateException("비활성화된 계정입니다.");
        }
        if ("Y".equals(user.getAcctLockYn())) {
            throw new IllegalStateException("잠긴 계정입니다. 관리자에게 문의하세요.");
        }
        if (!passwordEncoder.matches(password, user.getUserPwd())) {
            user.recordLoginFailure();
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        user.recordLoginSuccess();
        return user;
    }
}